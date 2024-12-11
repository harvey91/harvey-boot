package com.harvey.system.model.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.harvey.system.model.entity.BaseEntity;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.io.Serial;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
* 验证码 DTO类
*
* @author harvey
* @since 2024-12-11
*/
@Data
public class VerifyCodeDto {

    private Long id;

    @Schema(description = "联系方式")
    private String contact;

    @Schema(description = "联系类型")
    private Integer contactType;

    @Schema(description = "验证码")
    private String verifyCode;

    @Schema(description = "验证类型")
    private Integer verifyType;

    @Schema(description = "平台")
    private Integer platform;

    @Schema(description = "过期时间")
    private LocalDateTime expireTime;

    @Schema(description = "验证时间")
    private LocalDateTime verifyTime;
}
