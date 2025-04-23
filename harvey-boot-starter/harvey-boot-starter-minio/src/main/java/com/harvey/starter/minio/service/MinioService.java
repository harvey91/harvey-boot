package com.harvey.starter.minio.service;

import com.harvey.starter.minio.model.MinioResp;
import io.minio.MinioClient;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Minio 接口类
 * @author Harvey
 * @since 2025-04-23 20:38
 **/
public interface MinioService {

    /**
     * 获取公共MinioClient对象
     *
     * @return {@link MinioClient}
     */
    MinioClient getClient();

    /**
     * 桶是否存在
     * @param bucketName
     * @return
     */
    Boolean bucketExists(String bucketName);

    /**
     * 创建桶
     * @param bucketName
     */
    void makeBucket(String bucketName);

    /**
     * 从MinIO得到对象流
     * @param bucketName
     * @param objectName
     * @return
     */
    InputStream getObject(String bucketName, String objectName);

    /**
     * 从MinIO得到对象url（默认7天有效）
     * @param objectName
     * @return
     */
    String getObjectUrl(String bucketName, String objectName);

    /**
     * 从MinIO得到对象url，自定义过期时间
     * @param objectName
     * @param expiry
     * @param timeUnit
     * @return
     */
    String getObjectUrl(String bucketName, String objectName, int expiry, TimeUnit timeUnit);

    /**
     * 上传文件
     * @param stream
     * @param bucketName
     * @param objectName
     */
    MinioResp upload(InputStream stream, String bucketName, String objectName);

    /**
     * 删除文件
     * @param bucketName
     * @param objectName
     */
    void delete(String bucketName, String objectName);
}
