package com.harvey.system.storage;

import cn.hutool.core.date.DateUtil;
import lombok.Data;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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

    public void delete(String keyName) {
        storage.delete(keyName);
    }

    private String generateUrl(String keyName) {
        return storage.generateUrl(keyName);
    }
}
