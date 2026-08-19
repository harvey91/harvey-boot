package com.harvey.screen.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.harvey.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 信发设备
 *
 * @author Harvey
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("screen_device")
@Schema(title = "ScreenDevice", description = "信发设备表")
public class ScreenDevice extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(title = "设备编号")
    private String deviceNo;

    @Schema(title = "设备名称")
    private String deviceName;

    @Schema(title = "设备型号")
    private String model;

    @Schema(title = "登录令牌")
    private String secret;

    @Schema(title = "最后连接IP")
    private String ip;

    @Schema(title = "最后在线时间")
    private LocalDateTime lastOnlineTime;

    @Schema(title = "状态(0离线 1在线)，冗余镜像，实时状态以 TCP 会话为准")
    private Integer status;
}
