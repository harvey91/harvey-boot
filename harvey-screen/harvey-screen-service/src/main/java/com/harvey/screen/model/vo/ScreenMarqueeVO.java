package com.harvey.screen.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 信发滚动字幕模板视图
 *
 * @author Harvey
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "ScreenMarqueeVO")
public class ScreenMarqueeVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(title = "模板标题")
    private String title;

    @Schema(title = "字幕内容")
    private String content;

    @Schema(title = "滚动速度(1-20)")
    private Integer speed;

    @Schema(title = "文字颜色(十六进制)")
    private String color;

    @Schema(title = "字号")
    private Integer fontSize;

    @Schema(title = "循环次数(0循环)")
    private Integer repeatCount;

    @Schema(title = "创建时间")
    private LocalDateTime createTime;
}