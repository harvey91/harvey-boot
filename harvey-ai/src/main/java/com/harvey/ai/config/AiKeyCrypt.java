package com.harvey.ai.config;

import cn.hutool.crypto.symmetric.AES;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * API密钥加解密工具
 *
 * @author harvey
 * @since 2026-08-17
 */
@Component
public class AiKeyCrypt {

    private final AES aes;

    public AiKeyCrypt(@Value("${ai.api-key-secret:harvey-ai-secret}") String secret) {
        byte[] key = secret.getBytes(StandardCharsets.UTF_8);
        if (key.length != 16 && key.length != 24 && key.length != 32) {
            throw new IllegalStateException("ai.api-key-secret 长度必须为 16/24/32 字节");
        }
        this.aes = cn.hutool.crypto.SecureUtil.aes(key);
    }

    /**
     * 加密
     */
    public String encrypt(String plainText) {
        if (plainText == null || plainText.isBlank()) {
            return null;
        }
        return aes.encryptHex(plainText);
    }

    /**
     * 解密
     */
    public String decrypt(String cipherText) {
        if (cipherText == null || cipherText.isBlank()) {
            return "";
        }
        return aes.decryptStr(cipherText);
    }
}