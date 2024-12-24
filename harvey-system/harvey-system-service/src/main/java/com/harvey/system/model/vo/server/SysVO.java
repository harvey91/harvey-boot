package com.harvey.system.model.vo.server;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @author Harvey
 * @date 2024-12-04 17:20
 **/
@Schema(description = "系统相关信息")
@Data
@Builder
public class SysVO {

    @Schema(description = "系统信息")
    private String os;

    @Schema(description = "运行时间")
    private String day;

    @Schema(description = "IP地址")
    private String ip;
}
