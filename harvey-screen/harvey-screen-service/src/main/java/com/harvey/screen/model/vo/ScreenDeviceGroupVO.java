package com.harvey.screen.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 信发设备分组视图
 *
 * @author Harvey
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "ScreenDeviceGroupVO")
public class ScreenDeviceGroupVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(title = "分组名称")
    private String groupName;

    @Schema(title = "分组内设备数量")
    private Long deviceCount;

    @Schema(title = "描述")
    private String remark;

    @Schema(title = "排序")
    private Integer sort;

    @Schema(title = "是否启用")
    private Integer enabled;

    @Schema(title = "创建时间")
    private LocalDateTime createTime;
}