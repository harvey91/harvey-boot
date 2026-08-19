package com.harvey.screen.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.harvey.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 信发滚动字幕模板
 *
 * @author Harvey
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("screen_marquee")
@Schema(title = "ScreenMarquee", description = "信发滚动字幕模板表")
public class ScreenMarquee extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

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
}