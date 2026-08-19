package com.harvey.screen.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 信发滚动字幕模板
 *
 * @author Harvey
 */
@Data
@Schema(title = "ScreenMarqueeDto")
public class ScreenMarqueeDto {

    @Schema(title = "主键id")
    private Long id;

    @NotBlank(message = "模板标题不能为空")
    @Size(max = 64, message = "模板标题最长64个字符")
    @Schema(title = "模板标题")
    private String title;

    @NotBlank(message = "字幕内容不能为空")
    @Size(max = 200, message = "字幕内容最长200个字符")
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
}