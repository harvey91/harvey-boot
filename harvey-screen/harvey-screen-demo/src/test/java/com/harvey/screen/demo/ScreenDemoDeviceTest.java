package com.harvey.screen.demo;

import com.harvey.screen.api.AckMessage;
import com.harvey.screen.api.AckStatus;
import com.harvey.screen.api.LoginMessage;
import com.harvey.screen.api.ReportMessage;
import com.harvey.screen.api.ScreenCodec;
import com.harvey.screen.api.ScreenCommandCode;
import com.harvey.screen.api.ScreenConstants;
import com.harvey.screen.api.ScreenMessage;
import com.harvey.screen.api.ScreenMessageType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 设备端示例冒烟测试：用裸 ServerSocket + 共享编解码扮演服务端，
 * 验证 登录 -> 心跳 -> 指令执行(查询状态) -> 上报 + 应答 全链路。
 *
 * @author Harvey
 */
class ScreenDemoDeviceTest {

    private static final long COMMAND_SEQ = 555L;
    private static final long DEADLINE_MS = 15_000;

    private ScreenDemoDevice device;
    private ServerSocket server;

    @AfterEach
    void tearDown() throws IOException {
        if (device != null) {
            device.stop();
        }
        if (server != null) {
            server.close();
        }
    }

    @Test
    void loginHeartbeatAndExecuteCommand() throws Exception {
        AtomicLong loginSeq = new AtomicLong(-1);
        AtomicBoolean reportSeen = new AtomicBoolean(false);
        AtomicReference<Integer> reportType = new AtomicReference<>();
        AtomicLong ackSeq = new AtomicLong(-1);
        AtomicLong heartbeatCount = new AtomicLong();

        server = new ServerSocket(0);
        int port = server.getLocalPort();

        Thread serverThread = new Thread(() -> {
            try (Socket conn = server.accept()) {
                conn.setSoTimeout(5_000);
                InputStream in = conn.getInputStream();
                OutputStream out = conn.getOutputStream();
                long deadline = System.currentTimeMillis() + DEADLINE_MS;
                boolean commandSent = false;
                while (System.currentTimeMillis() < deadline) {
                    ScreenMessage msg;
                    try {
                        msg = readFrame(in);
                    } catch (SocketTimeoutException e) {
                        continue;
                    }
                    switch (ScreenMessageType.of(msg.getMessageType())) {
                        case LOGIN -> {
                            LoginMessage login = msg.payload(LoginMessage.class);
                            assertEquals("DEV-001", login.getDeviceNo());
                            assertEquals("test123", login.getToken());
                            loginSeq.set(msg.getSeq());
                            write(out, ScreenMessage.ack(nextSeq(),
                                    AckMessage.of(msg.getSeq(), AckStatus.OK, "login ok")));
                            // 登录应答后立即下发"查询状态"指令
                            Map<String, Object> params = new LinkedHashMap<>();
                            params.put("ask", "status");
                            write(out, ScreenMessage.command(COMMAND_SEQ, ScreenCommandCode.QUERY_STATUS, params));
                            commandSent = true;
                        }
                        case HEARTBEAT -> {
                            heartbeatCount.incrementAndGet();
                            write(out, ScreenMessage.ack(nextSeq(),
                                    AckMessage.of(msg.getSeq(), AckStatus.OK, "hb ok")));
                        }
                        case REPORT -> {
                            ReportMessage report = msg.payload(ReportMessage.class);
                            reportType.set(report.getReportType());
                            reportSeen.set(true);
                        }
                        case ACK -> {
                            AckMessage ack = msg.payload(AckMessage.class);
                            if (msg.getCmdCode() == 0) {
                                ackSeq.set(ack.getAckSeq());
                            }
                        }
                        default -> {
                        }
                    }
                }
                assertTrue(commandSent, "服务端应已下发指令");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, "fake-server");
        serverThread.start();

        device = new ScreenDemoDevice("127.0.0.1", port, "DEV-001", "test123", "LED-55X", 300);
        Thread deviceThread = new Thread(device::start, "demo-device");
        deviceThread.setDaemon(true);
        deviceThread.start();

        long deadline = System.currentTimeMillis() + DEADLINE_MS;
        while (System.currentTimeMillis() < deadline) {
            if (reportSeen.get() && ackSeq.get() == COMMAND_SEQ && heartbeatCount.get() > 0) {
                break;
            }
            Thread.sleep(100);
        }

        assertTrue(loginSeq.get() > 0, "服务端应收到登录报文");
        assertTrue(heartbeatCount.get() > 0, "登录成功后设备应持续心跳");
        assertTrue(reportSeen.get(), "设备应上报状态(查询状态指令)");
        assertNotNull(reportType.get());
        assertEquals(1, reportType.get(), "上报类型应为 1=状态");
        assertEquals(COMMAND_SEQ, ackSeq.get(), "设备应应答查询状态指令的报文序号");
    }

    /* ------------------- 裸 socket 帧读写(与设备端一致) ------------------- */

    private static ScreenMessage readFrame(InputStream in) throws IOException {
        byte[] header = readFully(in, ScreenConstants.HEADER_LENGTH);
        int length = ScreenCodec.readLength(header);
        byte[] tail = readFully(in, length + ScreenConstants.CRC_LENGTH);
        byte[] frame = new byte[header.length + tail.length];
        System.arraycopy(header, 0, frame, 0, header.length);
        System.arraycopy(tail, 0, frame, header.length, tail.length);
        return ScreenCodec.decode(frame);
    }

    private static byte[] readFully(InputStream in, int len) throws IOException {
        byte[] buf = new byte[len];
        int off = 0;
        while (off < len) {
            int n = in.read(buf, off, len - off);
            if (n < 0) {
                throw new IOException("连接被关闭");
            }
            off += n;
        }
        return buf;
    }

    private static void write(OutputStream out, ScreenMessage message) throws IOException {
        out.write(ScreenCodec.encode(message));
        out.flush();
    }

    private static final AtomicLong SERVER_SEQ = new AtomicLong(1000);

    private static long nextSeq() {
        return SERVER_SEQ.incrementAndGet();
    }
}
