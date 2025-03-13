package com.harvey.ai.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Harvey
 * @date 2025-03-13 18:34
 **/
@Data
public class MessageDto {

    @NotBlank(message = "不能发送空白消息")
    private String message;
}
