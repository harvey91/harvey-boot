package com.harvey.screen.tcp;

import com.harvey.screen.api.AckMessage;
import com.harvey.screen.api.AckStatus;
import com.harvey.screen.api.HeartbeatMessage;
import com.harvey.screen.api.LoginMessage;
import com.harvey.screen.api.ScreenCommandCode;
import com.harvey.screen.api.ScreenMessage;
import com.harvey.screen.api.ScreenMessageType;
import com.harvey.screen.tcp.session.ScreenSessionManager;
import com.harvey.screen.tcp.support.ScreenCommandSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 信发 TCP 服务集成测试：登录 -> 心跳 -> 指令下发/应答 -> 断线下线
 *
 * @author Harvey
 */
@SpringBootTest(classes = TestScreenTcpApplication.class, properties = {
        "harvey.screen.netty.port=0",
        "harvey.screen.netty.auth.required=true",
        "harvey.screen.netty.auth.token-whitelist=test-token"
})
class ScreenTcpServerTest {

    @Autowired
    private ScreenNettyServer server;

    @Autowired
    private ScreenSessionManager sessionManager;

    @Autowired
    private ScreenCommandSender commandSender;

    @Test
    void testLoginHeartbeatCommandRoundTrip() throws Exception {
        int port = server.getPort();
        assertThat(port).isGreaterThan(0);

        ScreenTestClient client = new ScreenTestClient();
        client.connect(new InetSocketAddress("127.0.0.1", port));
        try {
            // 1. 设备登录
            client.send(ScreenMessage.login(1L, new LoginMessage("TEST-001", "test-token", "TV-Simulator", System.currentTimeMillis())));
            ScreenMessage loginAck = client.await(ScreenMessageType.ACK, 5000);
            assertThat(loginAck).isNotNull();
            assertThat(loginAck.payload(AckMessage.class).getStatus()).isEqualTo(AckStatus.OK.getCode());
            assertThat(sessionManager.isOnline("TEST-001")).isTrue();

            // 2. 设备心跳
            client.send(ScreenMessage.heartbeat(2L, new HeartbeatMessage("TEST-001", System.currentTimeMillis())));
            ScreenMessage heartbeatAck = client.await(ScreenMessageType.ACK, 5000);
            assertThat(heartbeatAck).isNotNull();
            assertThat(heartbeatAck.payload(AckMessage.class).getStatus()).isEqualTo(AckStatus.OK.getCode());

            // 3. 服务端下发指令(音量加)
            assertThat(commandSender.sendCommand("TEST-001", ScreenCommandCode.VOLUME_UP, Collections.singletonMap("step", 1))).isTrue();
            ScreenMessage command = client.await(ScreenMessageType.COMMAND, 5000);
            assertThat(command).isNotNull();
            assertThat(command.getCmdCode()).isEqualTo(ScreenCommandCode.VOLUME_UP);
            assertThat(command.payload(com.harvey.screen.api.CommandMessage.class).getParams()).containsEntry("step", 1);

            // 4. 设备回执 ACK
            client.send(ScreenMessage.ack(3L, AckMessage.of(command.getSeq(), AckStatus.OK, "executed")));

            // 5. 服务端查询设备在线状态
            assertThat(sessionManager.onlineCount()).isEqualTo(1);
            assertThat(sessionManager.onlineDeviceNos()).contains("TEST-001");
        } finally {
            client.close();
        }

        // 6. 连接断开后设备下线
        Thread.sleep(500);
        assertThat(sessionManager.isOnline("TEST-001")).isFalse();
    }

    @Test
    void testCommandToOfflineDeviceFails() {
        boolean sent = commandSender.sendCommand("OFFLINE-001", ScreenCommandCode.POWER_ON, Map.of());
        assertThat(sent).isFalse();
    }
}