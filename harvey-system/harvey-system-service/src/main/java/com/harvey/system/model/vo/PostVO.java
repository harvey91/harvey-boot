package com.harvey.system.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author harvey
 * @since 2024-11-13
 */
@Data
@Schema(title = "PostVO")
public class PostVO {
    private Long id;

    @Schema(title = "postName", description = "职位名称")
    private String postName;

    @Schema(title = "postLevel", description = "职级")
    private Integer postLevel;

}
