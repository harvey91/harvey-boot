package com.harvey.system.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 下拉列表-可选树形
 * @author Harvey
 * @date 2024-10-30 13:54
 **/
@Schema(description = "下拉列表-可选树形")
@Data
@Builder
public class OptionVO<T> {

    @Schema(description = "值")
    private T value;

    @Schema(description = "键")
    private String label;

    @Schema(description = "子选项")
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private List<OptionVO<T>> children;

}
