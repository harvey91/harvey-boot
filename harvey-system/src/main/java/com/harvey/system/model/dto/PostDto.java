package com.harvey.system.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author harvey
 * @since 2024-11-13
 */
@Data
@Schema(title = "PostDto")
public class PostDto {

    private Long id;

    @NotBlank(message = "职位名称不能为空")
    @Schema(title = "postName", description = "职位名称")
    private String postName;

    @NotNull(message = "职级不能为空")
    @Schema(title = "postLevel", description = "职级")
    private Integer postLevel;

}
