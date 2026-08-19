package com.harvey.screen.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 信发媒体库视图
 *
 * @author Harvey
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "ScreenMediaVO")
public class ScreenMediaVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(title = "媒体名称")
    private String mediaName;

    @Schema(title = "媒体类型(1图片 2视频)")
    private Integer mediaType;

    @Schema(title = "访问地址")
    private String url;

    @Schema(title = "文件md5")
    private String md5;

    @Schema(title = "文件大小(字节)")
    private Long size;

    @Schema(title = "文件后缀")
    private String suffix;

    @Schema(title = "创建时间")
    private LocalDateTime createTime;
}