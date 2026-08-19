package com.harvey.screen.tcp.codec;

import com.harvey.screen.api.ScreenCodec;
import com.harvey.screen.api.ScreenCodecException;
import com.harvey.screen.api.ScreenConstants;
import com.harvey.screen.api.ScreenMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 信发报文解码器：负责粘包/拆包，读到完整帧后交给 {@link ScreenCodec} 校验解析
 *
 * @author Harvey
 */
@Slf4j
public class ScreenMessageDecoder extends ByteToMessageDecoder {

    private final int maxFrameLength;

    public ScreenMessageDecoder(int maxFrameLength) {
        this.maxFrameLength = maxFrameLength;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < ScreenConstants.HEADER_LENGTH) {
            return;
        }
        in.markReaderIndex();

        byte[] header = new byte[ScreenConstants.HEADER_LENGTH];
        in.readBytes(header);
        int length;
        try {
            length = ScreenCodec.readLength(header);
        } catch (ScreenCodecException e) {
            log.warn("非法报文头, 关闭连接: {}, error={}", ctx.channel().remoteAddress(), e.getMessage());
            ctx.close();
            return;
        }
        if (length < 0 || length > maxFrameLength) {
            log.warn("报文长度非法: length={}, max={}, 关闭连接: {}", length, maxFrameLength, ctx.channel().remoteAddress());
            ctx.close();
            return;
        }

        int bodyAndCrc = length + ScreenConstants.CRC_LENGTH;
        if (in.readableBytes() < bodyAndCrc) {
            in.resetReaderIndex();
            return;
        }

        byte[] frame = new byte[ScreenConstants.HEADER_LENGTH + bodyAndCrc];
        System.arraycopy(header, 0, frame, 0, header.length);
        in.readBytes(frame, header.length, bodyAndCrc);
        try {
            out.add(ScreenCodec.decode(frame));
        } catch (ScreenCodecException e) {
            log.warn("报文校验失败, 关闭连接: {}, error={}", ctx.channel().remoteAddress(), e.getMessage());
            ctx.close();
        }
    }
}