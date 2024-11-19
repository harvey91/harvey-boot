package com.harvey.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 系统部门表
 * </p>
 *
 * @author harvey
 * @since 2024-10-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dept")
@Schema(title = "Dept对象", description = "系统部门表")
public class Dept extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(title = "parentId",description = "父级id")
    private Long parentId;

    @Schema(title = "deptName",description = "部门名称")
    private String deptName;

    @Schema(title = "deptCode",description = "部门编号")
    private String deptCode;
}
