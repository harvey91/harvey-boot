package com.harvey.screen.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.harvey.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 信发媒体库
 *
 * @author Harvey
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("screen_media")
@Schema(title = "ScreenMedia", description = "信发媒体库表")
public class ScreenMedia extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

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
}