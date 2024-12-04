package com.harvey.system.model.vo.server;

import lombok.Builder;
import lombok.Data;

/**
 * @author Harvey
 * @date 2024-12-04 17:17
 **/
@Data
@Builder
public class ServerVO {

    /** 系统信息 */
    private SysVO sys;

    /** cpu信息 */
    private CpuVO cpu;

    /** 内存信息 */
    private StorageVO memory;

    /** 交换区信息 */
    private StorageVO swap;

    /** 磁盘信息 */
    private StorageVO disk;

    /** 当前时间 */
    private String time;
}
