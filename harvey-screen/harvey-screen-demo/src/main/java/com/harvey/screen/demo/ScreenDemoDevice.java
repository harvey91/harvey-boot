package com.harvey.screen.demo;

import com.harvey.screen.api.AckMessage;
import com.harvey.screen.api.AckStatus;
import com.harvey.screen.api.CommandMessage;
import com.harvey.screen.api.HeartbeatMessage;
import com.harvey.screen.api.LoginMessage;
import com.harvey.screen.api.MediaNotifyMessage;
import com.harvey.screen.api.ReportMessage;
import com.harvey.screen.api.ScreenCodec;
import com.harvey.screen.api.ScreenCommandCode;
import com.harvey.screen.api.ScreenConstants;
import com.harvey.screen.api.ScreenMessage;
import com.harvey.screen.api.ScreenMessageType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 信发设备端示例(模拟设备)
 * <p>
 * 纯 JDK Socket + 共享协议编解码(harvey-screen-api)，无需 Spring/Netty。
 * 实现：连接 -> 登录 -> 心跳(30s) -> 接收指令并模拟执行 -> 应答/上报，支持断线自动重连。
 * <p>
 * 启动参数：{@code host port deviceNo token [model]}
 * <pre>
 *   java -jar harvey-screen-demo.jar 127.0.0.1 9110 DEV-001 test123 LED-55X
 * </pre>
 *
 * @author Harvey
 */
public class ScreenDemoDevice {

    private static final int HEARTBEAT_INTERVAL_MS = 30_000;
    private static final int SOCKET_READ_TIMEOUT_MS = 30_000;
    private static final int RECONNECT_BACKOFF_MS = 5_000;

    private final String host;
    private final int port;
    private final String deviceNo;
    private final String token;
    private final String model;
    private final long heartbeatIntervalMs;

    private final AtomicLong seq = new AtomicLong();

    private Socket socket;
    private OutputStream out;
    private InputStream in;

    private volatile boolean running = true;
    private volatile boolean loggedIn;
    private volatile long loginSeq = -1;

    public ScreenDemoDevice(String host, int port, String deviceNo, String token, String model) {
        this(host, port, deviceNo, token, model, HEARTBEAT_INTERVAL_MS);
    }

    public ScreenDemoDevice(String host, int port, String deviceNo, String token, String model,
                            long heartbeatIntervalMs) {
        this.host = host;
        this.port = port;
        this.deviceNo = deviceNo;
        this.token = token;
        this.model = model;
        this.heartbeatIntervalMs = heartbeatIntervalMs;
    }

    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("用法: ScreenDemoDevice <host> <port> <deviceNo> <token> [model]");
            System.out.println("示例: ScreenDemoDevice 127.0.0.1 9110 DEV-001 test123 LED-55X");
            return;
        }
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String deviceNo = args[2];
        String token = args[3];
        String model = args.length > 4 ? args[4] : "LED-55X";
        new ScreenDemoDevice(host, port, deviceNo, token, model).start();
    }

    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> running = false));
        System.out.printf("信发模拟设备启动: host=%s port=%d deviceNo=%s model=%s%n", host, port, deviceNo, model);
        while (running) {
            try {
                connect();
                doLogin();
                Thread heartbeat = new Thread(this::heartbeatLoop, "demo-heartbeat-" + deviceNo);
                heartbeat.setDaemon(true);
                heartbeat.start();
                readLoop();
            } catch (Exception e) {
                if (running) {
                    System.err.println("[连接] 异常: " + e.getMessage());
                }
            } finally {
                closeQuietly();
                if (running) {
                    System.out.printf("[重连] %d 秒后重新连接...%n", RECONNECT_BACKOFF_MS / 1000);
                    sleep(RECONNECT_BACKOFF_MS);
                }
            }
        }
        System.out.println("设备已退出");
    }

    /** 停止设备(测试/关闭用) */
    public void stop() {
        running = false;
        closeQuietly();
    }

    /* ------------------- 连接与登录 ------------------- */

    private void connect() throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), 5_000);
        socket.setSoTimeout(SOCKET_READ_TIMEOUT_MS);
        socket.setTcpNoDelay(true);
        out = socket.getOutputStream();
        in = socket.getInputStream();
        System.out.println("[连接] 已连接 " + socket.getRemoteSocketAddress());
    }

    private void doLogin() throws IOException {
        loggedIn = false;
        loginSeq = seq.incrementAndGet();
        write(ScreenMessage.login(loginSeq,
                new LoginMessage(deviceNo, token, model, System.currentTimeMillis())));
        System.out.println("[登录] 已发送登录报文 seq=" + loginSeq + ", 等待应答...");
    }

    /* ------------------- 心跳 ------------------- */

    private void heartbeatLoop() {
        while (running) {
            try {
                if (loggedIn) {
                    write(ScreenMessage.heartbeat(seq.incrementAndGet(),
                            new HeartbeatMessage(deviceNo, System.currentTimeMillis())));
                }
                Thread.sleep(heartbeatIntervalMs);
            } catch (InterruptedException e) {
                break;
            } catch (Exception e) {
                System.err.println("[心跳] 发送失败: " + e.getMessage());
                break;
            }
        }
    }

    /* ------------------- 收报文 ------------------- */

    private void readLoop() throws IOException {
        while (running) {
            ScreenMessage message = readFrame();
            switch (ScreenMessageType.of(message.getMessageType())) {
                case ACK -> handleAck(message);
                case COMMAND -> handleCommand(message);
                case MEDIA_NOTIFY -> handleMediaNotify(message);
                default -> System.out.println("[收] 未处理消息 type=" + message.getMessageType()
                        + " seq=" + message.getSeq() + " cmdCode=" + message.getCmdCode());
            }
        }
    }

    private ScreenMessage readFrame() throws IOException {
        byte[] header = readFully(ScreenConstants.HEADER_LENGTH);
        int length = ScreenCodec.readLength(header);
        byte[] tail = readFully(length + ScreenConstants.CRC_LENGTH);
        byte[] frame = new byte[header.length + tail.length];
        System.arraycopy(header, 0, frame, 0, header.length);
        System.arraycopy(tail, 0, frame, header.length, tail.length);
        return ScreenCodec.decode(frame);
    }

    private byte[] readFully(int len) throws IOException {
        byte[] buf = new byte[len];
        int off = 0;
        while (off < len) {
            int n = in.read(buf, off, len - off);
            if (n < 0) {
                throw new IOException("连接被对端关闭");
            }
            off += n;
        }
        return buf;
    }

    /* ------------------- 指令处理 ------------------- */

    private void handleCommand(ScreenMessage message) {
        int cmdCode = message.getCmdCode();
        CommandMessage command = message.payload(CommandMessage.class);
        System.out.println("[指令] cmdCode=" + cmdCode + "(" + cmdName(cmdCode) + ") seq="
                + message.getSeq() + " params=" + (command == null ? null : command.getParams()));
        try {
            switch (cmdCode) {
                case ScreenCommandCode.POWER_ON -> {
                    simulate("开机", 500);
                    sendAck(message.getSeq(), AckStatus.OK, "设备已开机");
                }
                case ScreenCommandCode.POWER_OFF -> {
                    simulate("关机", 500);
                    sendAck(message.getSeq(), AckStatus.OK, "设备已关机");
                }
                case ScreenCommandCode.REBOOT -> {
                    simulate("重启", 1_500);
                    sendAck(message.getSeq(), AckStatus.OK, "设备已重启");
                }
                case ScreenCommandCode.VOLUME_UP, ScreenCommandCode.VOLUME_DOWN -> {
                    simulate("调节音量", 300);
                    sendAck(message.getSeq(), AckStatus.OK, "音量已调节");
                }
                case ScreenCommandCode.SCREENSHOT -> {
                    Map<String, Object> data = new LinkedHashMap<>();
                    data.put("imageBase64", "iVBORw0KGgoAAAANSUhEUgAA... 模拟截图数据");
                    data.put("width", 1920);
                    data.put("height", 1080);
                    report(3, data);
                    sendAck(message.getSeq(), AckStatus.OK, "截屏已回传");
                }
                case ScreenCommandCode.MARQUEE -> {
                    Map<String, Object> params = command == null ? Map.of() : command.getParams();
                    System.out.println("[字幕] " + params.get("content"));
                    simulate("展示滚动字幕", 1_000);
                    sendAck(message.getSeq(), AckStatus.OK, "字幕已展示");
                }
                case ScreenCommandCode.MEDIA_PUSH -> {
                    Map<String, Object> params = command == null ? Map.of() : command.getParams();
                    System.out.println("[媒体] 拉取并播放: " + params.get("url"));
                    simulate("播放媒体", 2_000);
                    sendAck(message.getSeq(), AckStatus.OK, "媒体已播放");
                }
                case ScreenCommandCode.QUERY_STATUS -> {
                    Map<String, Object> data = new LinkedHashMap<>();
                    data.put("power", "on");
                    data.put("volume", 45);
                    data.put("temperature", 38);
                    data.put("model", model);
                    report(1, data);
                    sendAck(message.getSeq(), AckStatus.OK, "状态已上报");
                }
                default -> sendAck(message.getSeq(), AckStatus.UNKNOWN_CMD, "不支持的指令");
            }
        } catch (IOException e) {
            System.err.println("[指令] 应答失败: " + e.getMessage());
        }
    }

    private void handleMediaNotify(ScreenMessage message) {
        MediaNotifyMessage notify = message.payload(MediaNotifyMessage.class);
        System.out.println("[媒体通知] mediaId=" + (notify == null ? null : notify.getMediaId())
                + " url=" + (notify == null ? null : notify.getUrl()));
        try {
            simulate("播放通知媒体", 1_000);
            sendAck(message.getSeq(), AckStatus.OK, "媒体通知已处理");
        } catch (IOException e) {
            System.err.println("[媒体通知] 应答失败: " + e.getMessage());
        }
    }

    private void handleAck(ScreenMessage message) {
        AckMessage ack = message.payload(AckMessage.class);
        System.out.println("[应答] ackSeq=" + ack.getAckSeq() + " status=" + ack.getStatus()
                + "(" + ack.getMessage() + ")");
        if (ack.getAckSeq() == loginSeq) {
            loggedIn = ack.getStatus() == AckStatus.OK.getCode();
            System.out.println(loggedIn ? "[登录] 成功" : "[登录] 失败, 拒绝登录: " + ack.getMessage());
        }
    }

    /* ------------------- 发送 ------------------- */

    private void write(ScreenMessage message) throws IOException {
        out.write(ScreenCodec.encode(message));
        out.flush();
    }

    private void sendAck(long ackSeq, AckStatus status, String msg) throws IOException {
        write(ScreenMessage.ack(seq.incrementAndGet(), AckMessage.of(ackSeq, status, msg)));
    }

    private void report(int reportType, Map<String, Object> data) throws IOException {
        write(ScreenMessage.report(seq.incrementAndGet(),
                new ReportMessage(reportType, deviceNo, System.currentTimeMillis(), data)));
        System.out.println("[上报] 已上报 type=" + reportType + " data=" + data);
    }

    /* ------------------- 工具 ------------------- */

    private static String cmdName(int cmdCode) {
        return switch (cmdCode) {
            case ScreenCommandCode.POWER_ON -> "开机";
            case ScreenCommandCode.POWER_OFF -> "关机";
            case ScreenCommandCode.REBOOT -> "重启";
            case ScreenCommandCode.VOLUME_UP -> "音量加";
            case ScreenCommandCode.VOLUME_DOWN -> "音量减";
            case ScreenCommandCode.SCREENSHOT -> "截屏";
            case ScreenCommandCode.MARQUEE -> "滚动字幕";
            case ScreenCommandCode.MEDIA_PUSH -> "媒体推送";
            case ScreenCommandCode.QUERY_STATUS -> "查询状态";
            default -> "指令(" + cmdCode + ")";
        };
    }

    private static void simulate(String action, long ms) {
        System.out.println("[执行] " + action + " ...");
        sleep(ms);
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void closeQuietly() {
        loggedIn = false;
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ignored) {
        }
    }
}