package com.harvey.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.harvey.core.model.BaseEntity;

import java.time.LocalDateTime;
import java.io.Serial;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 验证码
 * </p>
 *
 * @author harvey
 * @since 2024-12-11
 */
@Data
@EqualsAndHashCode(callSuper=true)
@TableName("sys_verify_code")
@Schema(name = "VerifyCode", description = "验证码")
public class VerifyCode extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户id")
    private Long userId;

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
}
