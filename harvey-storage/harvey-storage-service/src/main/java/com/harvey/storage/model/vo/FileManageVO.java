package com.harvey.storage.model.vo;

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

    @Schema(description = "上传平台")
    private String platform;

    @Schema(description = "文件路径")
    private String url;
}
