package com.harvey.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.harvey.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 系统字典数据表
 * </p>
 *
 * @author harvey
 * @since 2024-10-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_data")
@Schema(title = "DictData对象", description = "系统字典数据表")
public class DictData extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(title = "dictCode", description = "字典编码")
    private String dictCode;

    @Schema(title = "label", description = "字典项")
    private String label;

    @Schema(title = "value", description = "字典值")
    private String value;

    @Schema(title = "tag", description = "前端标签tag值")
    private String tag;

}
