package com.harvey.system.storage;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;
import com.harvey.system.model.dto.FileManageDto;
import com.harvey.system.service.FileManageService;
import com.harvey.system.utils.StringUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.LocalDateTime;
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

    public FileManageDto store(MultipartFile file, Long userId, String platform) throws IOException {
        long fileSize = file.getSize();
        String fileType = file.getContentType();
        String fileName = file.getOriginalFilename();
        String suffix = FileUtil.getSuffix(fileName);
        // 根据日期划分文件夹
        String folder = DateUtil.format(LocalDateTime.now(), "yyyyMMdd");
        // InputStream会被读取流，不可和存储共用一个流
        String md5 = SecureUtil.md5(file.getInputStream()).toUpperCase();
        String key = folder + File.separator + md5 + "." + suffix;
        String url = generateUrl(key);
        // 文件存储
        storage.store(file.getInputStream(), fileSize, fileType, key);
        // 文件信息入库管理
        FileManageDto fileManageDto = new FileManageDto();
        fileManageDto.setName(fileName);
        fileManageDto.setSize(fileSize);
        fileManageDto.setPlatform(platform);
        fileManageDto.setMd5(md5);
        fileManageDto.setUrl(url);
        fileManageDto.setSuffix(suffix);
        fileManageDto.setUserId(userId);
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
