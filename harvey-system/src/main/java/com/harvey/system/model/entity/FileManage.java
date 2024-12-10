package com.harvey.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.harvey.system.model.entity.BaseEntity;
import java.io.Serializable;
import java.io.Serial;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 文件管理
 * </p>
 *
 * @author harvey
 * @since 2024-12-05
 */
@Data
@EqualsAndHashCode(callSuper=true)
@TableName("sys_file_manage")
@Schema(name = "FileManage", description = "文件管理")
public class FileManage extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "上传者id")
    private Long userId;

    @Schema(description = "文件名")
    private String name;

    @Schema(description = "MD5")
    private String md5;

    @Schema(description = "文件大小")
    private Long size;

    @Schema(description = "文件后缀")
    private String suffix;

    @Schema(description = "上传平台")
    private String platform;

    @Schema(description = "文件路径")
    private String path;
}
