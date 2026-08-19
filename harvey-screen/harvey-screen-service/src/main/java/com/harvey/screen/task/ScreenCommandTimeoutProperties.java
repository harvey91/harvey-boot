package com.harvey.screen.task;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 指令超时轮询配置
 *
 * @author Harvey
 */
@Data
@Component
@ConfigurationProperties(prefix = "harvey.screen.command-timeout")
public class ScreenCommandTimeoutProperties {

    /** 是否启用指令超时轮询 */
    private boolean enabled = true;

    /** 指令进入"已发送"后，超过该秒数无应答判定为超时 */
    private long timeoutSeconds = 30;

    /** 轮询扫描间隔(秒) */
    private long scanIntervalSeconds = 10;
}