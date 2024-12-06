package com.harvey.system.storage;

import cn.hutool.crypto.SecureUtil;
import com.harvey.system.model.dto.FileManageDto;
import com.harvey.system.service.FileManageService;
import com.harvey.system.utils.StringUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @author Harvey
 * @date 2024-12-05 22:46
 **/
@Data
public class StorageService {
    @Autowired
    private FileManageService fileManageService;

    private String active;
    private IStorage storage;

    public FileManageDto store(InputStream inputStream, long contentLength, String contentType, String fileName) {
        String suffix = "";
        if (StringUtils.isNotBlank(fileName) && fileName.contains(".")) {
            int index = fileName.lastIndexOf('.') + 1;
            suffix = fileName.substring(index).toLowerCase();
        }
        String md5 = SecureUtil.md5(inputStream).toUpperCase();
        String key = md5 + "." + suffix;
        storage.store(inputStream, contentLength, contentType, key);
        String url = generateUrl(key);

        FileManageDto fileManageDto = new FileManageDto();
        fileManageDto.setName(fileName);
        fileManageDto.setSize(contentLength);
        fileManageDto.setType(contentType);
        fileManageDto.setMd5(md5);
        fileManageDto.setPath(url);
        fileManageDto.setSuffix(suffix);
        fileManageService.saveFileManage(fileManageDto);

        return fileManageDto;
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
