package com.harvey.system.model.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.harvey.system.model.entity.BaseEntity;
import java.io.Serializable;
import java.io.Serial;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
/**
* 文件管理 VO类
*
* @author harvey
* @since 2024-12-05
*/
@Data
public class FileManageVO {

    private Long id;

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

    @Schema(description = "类型")
    private String type;

    @Schema(description = "文件路径")
    private String path;
}
