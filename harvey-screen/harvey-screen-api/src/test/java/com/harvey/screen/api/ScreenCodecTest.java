package com.harvey.screen.api;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScreenCodecTest {

    @Test
    void testEncodeDecodeRoundTrip() {
        ScreenMessage message = ScreenMessage.command(123L, ScreenCommandCode.POWER_ON, Map.of("level", 1));

        byte[] frame = ScreenCodec.encode(message);
        ScreenMessage decoded = ScreenCodec.decode(frame);

        assertThat(decoded.getMagic()).isEqualTo(ScreenConstants.MAGIC);
        assertThat(decoded.getVersion()).isEqualTo(ScreenConstants.VERSION);
        assertThat(decoded.getMessageType()).isEqualTo(ScreenMessageType.COMMAND.getCode());
        assertThat(decoded.getSeq()).isEqualTo(123L);
        assertThat(decoded.getCmdCode()).isEqualTo(ScreenCommandCode.POWER_ON);
        assertThat(decoded.getCrc32()).isEqualTo(message.getCrc32());

        CommandMessage body = decoded.payload(CommandMessage.class);
        assertThat(body.getParams()).containsEntry("level", 1);
    }

    @Test
    void testLoginRoundTrip() {
        ScreenMessage message = ScreenMessage.login(1L, new LoginMessage("TEST-001", "token", "TV-Simulator", 1000L));
        ScreenMessage decoded = ScreenCodec.decode(ScreenCodec.encode(message));
        LoginMessage body = decoded.payload(LoginMessage.class);
        assertThat(body.getDeviceNo()).isEqualTo("TEST-001");
        assertThat(body.getToken()).isEqualTo("token");
        assertThat(decoded.is(ScreenMessageType.LOGIN)).isTrue();
    }

    @Test
    void testDecodeRejectsTamperedBody() {
        ScreenMessage message = ScreenMessage.command(1L, ScreenCommandCode.REBOOT, null);
        byte[] frame = ScreenCodec.encode(message);
        frame[ScreenConstants.HEADER_LENGTH] ^= 0x01;
        assertThatThrownBy(() -> ScreenCodec.decode(frame)).isInstanceOf(ScreenCodecException.class);
    }

    @Test
    void testDecodeRejectsWrongMagic() {
        ScreenMessage message = ScreenMessage.heartbeat(1L, new HeartbeatMessage("TEST-001", 1000L));
        byte[] frame = ScreenCodec.encode(message);
        frame[0] ^= 0x01;
        assertThatThrownBy(() -> ScreenCodec.decode(frame)).isInstanceOf(ScreenCodecException.class);
    }

    @Test
    void testEncodeRejectsOversizedBody() {
        ScreenMessage message = ScreenMessage.of(ScreenMessageType.REPORT.getCode(), 1L, 0, new byte[ScreenConstants.DEFAULT_MAX_FRAME_LENGTH + 1]);
        assertThatThrownBy(() -> ScreenCodec.encode(message)).isInstanceOf(ScreenCodecException.class);
    }
}