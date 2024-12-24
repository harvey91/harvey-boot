package com.harvey.system.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
/**
* 登陆日志表 VO类
*
* @author harvey
* @since 2024-11-21
*/
@Data
public class LogLoginVO {

    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "IP")
    private String ip;

    @Schema(description = "地点")
    private String location;

    @Schema(description = "浏览器")
    private String browser;

    @Schema(description = "操作系统")
    private String os;

    @Schema(description = "结果")
    private Integer result;

    @Schema(description = "备注")
    private String remark;
}
