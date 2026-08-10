package com.harvey.system.model.db;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 代码生成预览对象
 *
 * @author harvey
 * @since 2026-08-10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "GeneratorPreviewVO", description = "代码生成预览对象")
public class GeneratorPreviewVO {

    /** 文件生成路径 */
    @Schema(title = "path", description = "文件生成路径")
    private String path;

    /** 文件名称 */
    @Schema(title = "fileName", description = "文件名称")
    private String fileName;

    /** 文件内容 */
    @Schema(title = "content", description = "文件内容")
    private String content;

}
