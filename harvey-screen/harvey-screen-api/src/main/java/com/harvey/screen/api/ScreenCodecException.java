package com.harvey.screen.api;

/**
 * 信发协议编解码异常
 *
 * @author Harvey
 */
public class ScreenCodecException extends RuntimeException {

    public ScreenCodecException(String message) {
        super(message);
    }

    public ScreenCodecException(String message, Throwable cause) {
        super(message, cause);
    }
}