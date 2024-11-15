package com.harvey.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 系统职位表
 * </p>
 *
 * @author harvey
 * @since 2024-11-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_post")
@Schema(title = "SysPost对象", description = "系统职位表")
public class SysPost extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(title = "postName", description = "职位名称")
    private String postName;

    @Schema(title = "postLevel", description = "职级")
    private Integer postLevel;

}
