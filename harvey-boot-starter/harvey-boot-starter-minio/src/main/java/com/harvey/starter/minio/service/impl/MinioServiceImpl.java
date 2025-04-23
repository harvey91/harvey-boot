package com.harvey.starter.minio.service.impl;

import com.harvey.starter.minio.model.MinioResp;
import com.harvey.starter.minio.service.MinioService;
import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

/**
 * @author Harvey
 * @since 2025-04-23 20:41
 **/
@Slf4j
@Service
public class MinioServiceImpl implements MinioService {

    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MinioClient innerMinioClient;

    @Override
    public MinioClient getClient() {
        return this.minioClient;
    }

    @Override
    public Boolean bucketExists(String bucketName) {
        try {
            return innerMinioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void makeBucket(String bucketName) {
        try {
            if (bucketExists(bucketName)) {
                log.debug("存储桶：{}已存在,无需重新创建！", bucketName);
                return;
            }
            innerMinioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            log.error("令牌桶创建异常！{}", e.getMessage(), e);
            throw new RuntimeException("令牌桶创建失败！原因：" + e.getMessage());
        }
    }

    @Override
    public MinioResp upload(InputStream stream, String bucketName, String objectName) {
        try {
            int available = stream.available();
            PutObjectArgs putObjectArgs = PutObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(stream, available, -1)
                    .build();
            ObjectWriteResponse objectWriteResponse = innerMinioClient.putObject(putObjectArgs);

            BigDecimal decimal = BigDecimal.valueOf((double) available / 1048576);
            return MinioResp.builder()
                    .objectName(objectName).bucketName(objectWriteResponse.bucket())
                    .previewUrl(getObjectUrl(bucketName, objectName))
                    .fileMbSize(decimal.setScale(2, RoundingMode.HALF_UP).doubleValue())
                    .build();
        } catch (Exception e) {
            log.error("上传文件到MinIO失败！", e);
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public InputStream getObject(String bucketName, String objectName) {
        try {
            return innerMinioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName).object(objectName)
                    .build());
        } catch (Exception e) {
            log.error("从MinIO获取文件失败！", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String getObjectUrl(String bucketName, String objectName) {
        return getObjectUrl(bucketName, objectName, 7, TimeUnit.DAYS);
    }

    @Override
    public String getObjectUrl(String bucketName, String objectName, int expiry, TimeUnit timeUnit) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName).object(objectName)
                    .expiry(expiry, timeUnit)
                    .method(Method.GET)
                    .build());
        } catch (Exception e) {
            log.error("从MinIO获取文件URL失败！", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(String bucketName, String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName).object(objectName)
                    .build());
        } catch (Exception e) {
            log.error("从MinIO删除文件失败！", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
