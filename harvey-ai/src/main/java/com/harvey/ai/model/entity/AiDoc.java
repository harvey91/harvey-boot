package com.harvey.ai.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.harvey.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * AI知识库文档表
 * </p>
 *
 * @author harvey
 * @since 2026-08-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_doc")
@Schema(title = "AiDoc对象", description = "AI知识库文档表")
public class AiDoc extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(title = "kbId", description = "知识库ID")
    private Long kbId;

    @Schema(title = "fileName", description = "文件名")
    private String fileName;

    @Schema(title = "fileType", description = "文件类型, 如 txt/md/pdf/docx")
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
    private LocalDateTime parseTime;

}