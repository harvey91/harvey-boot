package com.harvey.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 系统字典表
 * </p>
 *
 * @author harvey
 * @since 2024-10-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict")
@Schema(title = "Dict对象", description = "系统字典表")
public class Dict extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(title = "dictCode",description = "字典编码")
    private String dictCode;

    @Schema(title = "dictName",description = "字典名称")
    private String dictName;

}
