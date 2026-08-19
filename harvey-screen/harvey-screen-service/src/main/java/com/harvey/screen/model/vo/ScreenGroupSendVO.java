package com.harvey.screen.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分组批量下发结果
 *
 * @author Harvey
 */
@Data
@AllArgsConstructor
@Schema(title = "ScreenGroupSendVO")
public class ScreenGroupSendVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "目标设备总数")
    private Integer total;

    @Schema(title = "下发成功数量")
    private Integer success;

    @Schema(title = "下发失败数量")
    private Integer failed;
}