package com.harvey.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 系统配置表
 * </p>
 *
 * @author harvey
 * @since 2024-11-13
 */
@Data
@TableName("sys_config")
@Schema(title = "SysConfig对象", description = "系统配置表")
public class SysConfig extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(title = "configName", description = "配置名称")
    private String configName;

    @Schema(title = "configKey", description = "配置键key")
    private String configKey;

    @Schema(title = "configValue", description = "配置值value")
    private String configValue;

}
