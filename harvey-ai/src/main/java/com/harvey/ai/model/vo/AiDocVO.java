package com.harvey.ai.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author harvey
 * @since 2026-08-17
 */
@Data
@Schema(title = "AiDocVO")
public class AiDocVO {
    private Long id;

    @Schema(title = "kbId", description = "知识库ID")
    private Long kbId;

    @Schema(title = "fileName", description = "文件名")
    private String fileName;

    @Schema(title = "fileType", description = "文件类型")
    private String fileType;

    @Schema(title = "fileSize", description = "文件大小(字节)")
    private Long fileSize;

    @Schema(title = "fileUrl", description = "文件存储地址")
    private String fileUrl;

    @Schema(title = "chunkCount", description = "分块数量")
    private Integer chunkCount;

    @Schema(title = "status", description = "解析状态(0待解析,1解析中,2就绪,3失败)")
    private Integer status;

    @Schema(title = "parseTime", description = "解析完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime parseTime;

    @Schema(title = "createTime", description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}