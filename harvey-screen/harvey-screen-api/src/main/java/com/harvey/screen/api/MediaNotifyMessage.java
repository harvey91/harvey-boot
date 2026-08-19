package com.harvey.screen.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 媒体/大消息通知消息体
 * <p>
 * 大消息走 HTTP/MinIO，TCP 仅下发元数据与下载地址，设备自行拉取。
 *
 * @author Harvey
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaNotifyMessage {

    /** 媒体ID */
    private String mediaId;

    /** 媒体类型(1=图片 2=视频 3=滚动字幕模板) */
    private int mediaType;

    /** 下载地址 */
    private String url;

    /** 文件 MD5 */
    private String md5;

    /** 文件大小(字节) */
    private long size;

    /** 客户端时间戳(ms) */
    private long timestamp;
}