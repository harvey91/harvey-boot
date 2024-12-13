package com.harvey.system.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Harvey
 * @date 2024-10-29 18:36
 **/
@Data
@Schema(title = "DeptVO")
public class DeptVO {

    private Long id;

    @Schema(title = "parentId",description = "父级id")
    private Long parentId;

    @Schema(title = "deptName",description = "部门名称")
    private String deptName;

    @Schema(title = "deptCode",description = "部门编号")
    private String deptCode;

    @Schema(title = "sort", description = "排序", defaultValue = "99")
    private Integer sort;

    @Schema(title = "enabled", description = "是否启用(0禁用，1启用)", defaultValue = "1")
    private Integer enabled;

    @Schema(title = "createTime", description = "创建时间")
    public LocalDateTime createTime;

    private List<DeptVO> children;
}
