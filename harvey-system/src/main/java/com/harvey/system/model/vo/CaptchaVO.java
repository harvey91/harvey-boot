package com.harvey.system.model.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author Harvey
 * @date 2024-11-20 13:30
 **/
@Data
@Builder
public class CaptchaVO {

    private String captchaKey;

    private String captchaBase64;
}
