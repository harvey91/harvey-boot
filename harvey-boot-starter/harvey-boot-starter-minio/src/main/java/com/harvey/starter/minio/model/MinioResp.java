package com.harvey.starter.minio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * minio 响应
 * @author Harvey
 * @since 2025-04-23 20:56
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MinioResp {
    /**
     * 文件的名称
     */
    private String fileName;
    /**
     * 保存到Minio的名字（唯一）
     */
    private String objectName;
    /**
     * 预览地址
     */
    private String previewUrl;

    /**
     * 存储桶名称
     */
    private String bucketName;
    /**
     * 文件大小
     */
    private Double fileMbSize;
}
