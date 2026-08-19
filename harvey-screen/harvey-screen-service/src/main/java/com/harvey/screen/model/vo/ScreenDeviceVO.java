package com.harvey.screen.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 信发设备视图
 *
 * @author Harvey
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "ScreenDeviceVO")
public class ScreenDeviceVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(title = "设备编号")
    private String deviceNo;

    @Schema(title = "设备名称")
    private String deviceName;

    @Schema(title = "设备型号")
    private String model;

    @Schema(title = "最后连接IP")
    private String ip;

    @Schema(title = "最后在线时间")
    private LocalDateTime lastOnlineTime;

    @Schema(title = "是否在线(TCP 实时状态)")
    private Boolean online;

    @Schema(title = "状态(0离线 1在线)")
    private Integer status;

    @Schema(title = "描述")
    private String remark;

    @Schema(title = "排序")
    private Integer sort;

    @Schema(title = "是否启用")
    private Integer enabled;

    @Schema(title = "创建时间")
    private LocalDateTime createTime;
}
