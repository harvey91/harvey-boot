package com.harvey.core.storage;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
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
 * 腾讯云对象存储服务
 * @author Harvey
 * @date 2024-12-05 22:27
 **/
@Slf4j
@Data
public class TencentStorage implements IStorage {

    private String secretId;
    private String secretKey;
    private String region;
    private String bucketName;

    private COSClient cosClient;

    private COSClient getCOSClient() {
        if (cosClient == null) {
            // 1 初始化用户身份信息(secretId, secretKey)
            COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
            // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
            ClientConfig clientConfig = new ClientConfig(new Region(region));
            cosClient = new COSClient(cred, clientConfig);
        }

        return cosClient;
    }

    private String getBaseUrl() {
        return "https://" + bucketName + ".cos." + region + ".myqcloud.com/";
    }

    @Override
    public void store(InputStream inputStream, long contentLength, String contentType, String keyName) {
        try {
            // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20M以下的文件使用该接口
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(contentLength);
            objectMetadata.setContentType(contentType);
            // 对象键（Key）是对象在存储桶中的唯一标识。例如，在对象的访问域名 `bucket1-1250000000.cos.ap-guangzhou.myqcloud.com/doc1/pic1.jpg`
            // 中，对象键为 doc1/pic1.jpg, 详情参考 [对象键](https://cloud.tencent.com/document/product/436/13324)
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, keyName, inputStream, objectMetadata);
            getCOSClient().putObject(putObjectRequest);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
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
            URL url = new URL(getBaseUrl() + keyName);
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
            getCOSClient().deleteObject(bucketName, keyName);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    @Override
    public String generateUrl(String keyName) {
        return getBaseUrl() + keyName;
    }
}
