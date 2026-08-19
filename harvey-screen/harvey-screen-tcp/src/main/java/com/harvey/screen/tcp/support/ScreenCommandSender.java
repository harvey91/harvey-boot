package com.harvey.screen.tcp.support;

import com.harvey.screen.api.ScreenMessage;
import com.harvey.screen.tcp.session.ScreenSession;
import com.harvey.screen.tcp.session.ScreenSessionManager;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 指令下发器：按设备编号向在线设备下发指令
 *
 * @author Harvey
 */
@Slf4j
@Component
public class ScreenCommandSender {

    private final ScreenSessionManager sessionManager;
    private final ScreenSeqGenerator seqGenerator;

    public ScreenCommandSender(ScreenSessionManager sessionManager, ScreenSeqGenerator seqGenerator) {
        this.sessionManager = sessionManager;
        this.seqGenerator = seqGenerator;
    }

    /**
     * 向设备下发指令
     *
     * @param deviceNo 设备编号
     * @param cmdCode  指令编码，见 {@link com.harvey.screen.api.ScreenCommandCode}
     * @param params   指令参数
     * @return true=已写入连接(设备是否执行以 ACK 为准)
     */
    public boolean sendCommand(String deviceNo, int cmdCode, Map<String, Object> params) {
        return send(deviceNo, ScreenMessage.command(seqGenerator.next(), cmdCode, params));
    }

    /**
     * 向设备下发原始报文
     */
    public boolean send(String deviceNo, ScreenMessage message) {
        ScreenSession session = sessionManager.get(deviceNo);
        if (session == null) {
            log.warn("设备不在线, 无法下发: deviceNo={}, type={}, cmdCode={}", deviceNo, message.getMessageType(), message.getCmdCode());
            return false;
        }
        Channel channel = session.getChannel();
        if (!channel.isActive()) {
            sessionManager.unregister(channel);
            return false;
        }
        channel.writeAndFlush(message);
        log.info("指令下发成功: deviceNo={}, type={}, cmdCode={}, seq={}", deviceNo, message.getMessageType(), message.getCmdCode(), message.getSeq());
        return true;
    }
}