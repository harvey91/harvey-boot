package com.harvey.ai.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Harvey
 * @date 2025-03-04 16:38
 **/
@Data
public class MessageDto {

    @NotBlank(message = "不能发送空白消息")
    private String message;
}
