package com.harvey.screen.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.harvey.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 信发设备分组
 *
 * @author Harvey
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("screen_device_group")
@Schema(title = "ScreenDeviceGroup", description = "信发设备分组表")
public class ScreenDeviceGroup extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(title = "分组名称")
    private String groupName;
}