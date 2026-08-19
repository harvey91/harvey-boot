package com.harvey.screen.model.query;

import com.harvey.common.model.query.Query;
import lombok.Data;

/**
 * 信发媒体库分页查询
 *
 * @author Harvey
 */
@Data
public class ScreenMediaQuery extends Query {

    /** 媒体类型(1图片 2视频) */
    private Integer mediaType;
}