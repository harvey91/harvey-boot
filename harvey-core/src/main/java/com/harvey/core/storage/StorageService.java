package com.harvey.core.storage;

import cn.hutool.core.date.DateUtil;
import lombok.Data;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.stream.Stream;

/**
 * @author Harvey
 * @date 2024-12-05 22:46
 **/
@Data
public class StorageService {

    private String active;
    private String address;
    private IStorage storage;

    public String store(MultipartFile file, String md5, String suffix) throws IOException {
        long fileSize = file.getSize();
        String fileType = file.getContentType();
        // 根据日期划分文件夹
        String folder = DateUtil.format(LocalDateTime.now(), "yyyyMMdd");
        String key = folder + File.separator + md5 + "." + suffix;
        // 文件存储
        storage.store(file.getInputStream(), fileSize, fileType, key);
        return generateUrl(key);
    }

    public Stream<Path> loadAll() {
        return storage.loadAll();
    }

    public Path load(String keyName) {
        return storage.load(keyName);
    }

    public Resource loadAsResource(String keyName) {
        return storage.loadAsResource(keyName);
    }

    /**
     * 根据归档 URL 读取文件字节(用于重新解析等场景)
     *
     * @param url 存储归档 URL, 形如 /storage/fetch/20260817/md5.txt
     */
    public byte[] loadBytes(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("归档文件URL不能为空");
        }
        String key = url;
        if (address != null && url.startsWith(address)) {
            key = url.substring(address.length());
        }
        key = key.replaceFirst("^[/\\\\]+", "");
        try {
            return storage.getBytes(key);
        } catch (Exception e) {
            throw new RuntimeException("读取存储文件失败: " + url, e);
        }
    }

    public void delete(String keyName) {
        storage.delete(keyName);
    }

    private String generateUrl(String keyName) {
        return storage.generateUrl(keyName);
    }
}
