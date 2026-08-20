package com.harvey.storage.oss;

import cn.hutool.core.io.IoUtil;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Minio 对象存储服务
 *
 * @author Harvey
 * @date 2026-08-20
 **/
@Slf4j
@Data
public class MinioStorage implements IStorage {
    private String endpoint;
    private String innerEndpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
    private String address;

    private MinioClient minioClient;
    private MinioClient innerMinioClient;

    private MinioClient getMinioClient() {
        if (minioClient == null) {
            minioClient = MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
        }
        return minioClient;
    }

    private MinioClient getInnerMinioClient() {
        if (innerMinioClient == null) {
            String ep = innerEndpoint != null && !innerEndpoint.isBlank() ? innerEndpoint : endpoint;
            innerMinioClient = MinioClient.builder().endpoint(ep).credentials(accessKey, secretKey).build();
        }
        return innerMinioClient;
    }

    @Override
    public void store(InputStream inputStream, long contentLength, String contentType, String keyName) {
        try {
            MinioClient client = getMinioClient();
            if (!client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            client.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(keyName)
                    .stream(inputStream, contentLength, -1)
                    .contentType(contentType)
                    .build());
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new RuntimeException("Minio上传失败: " + keyName, ex);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    public Path load(String keyName) {
        return null;
    }

    @Override
    public Resource loadAsResource(String keyName) {
        try {
            URL url = new URL(generateUrl(keyName));
            Resource resource = new UrlResource(url);
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void delete(String keyName) {
        try {
            getInnerMinioClient().removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(keyName).build());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public String generateUrl(String keyName) {
        return address.endsWith("/") ? address + keyName : address + "/" + keyName;
    }

    @Override
    public byte[] getBytes(String keyName) {
        String key = keyName;
        if (address != null && key.startsWith(address)) {
            key = key.substring(address.length());
        }
        key = key.replaceFirst("^[/\\\\]+", "");
        try (InputStream in = getInnerMinioClient().getObject(GetObjectArgs.builder()
                .bucket(bucketName).object(key).build())) {
            return IoUtil.readBytes(in);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new RuntimeException("读取Minio文件失败: " + keyName, ex);
        }
    }
}
