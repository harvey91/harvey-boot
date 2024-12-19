package com.harvey.system.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.common.exception.BadParameterException;
import com.harvey.system.mapper.FileManageMapper;
import com.harvey.system.mapstruct.FileManageConverter;
import com.harvey.system.model.dto.FileManageDto;
import com.harvey.system.model.entity.FileManage;
import com.harvey.system.model.query.FileManageQuery;
import com.harvey.common.utils.StringUtils;
import com.harvey.core.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 文件管理 服务实现类
 *
 * @author harvey
 * @since 2024-12-05
 */
@Service
@RequiredArgsConstructor
public class FileManageService extends ServiceImpl<FileManageMapper, FileManage> {
    private final FileManageMapper mapper;
    private final FileManageConverter converter;
    private final StorageService storageService;

    public Page<FileManage> queryPage(FileManageQuery query) {
        Page<FileManage> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<FileManage> queryWrapper = new LambdaQueryWrapper<FileManage>()
                .like(StringUtils.isNotBlank(query.getKeywords()), FileManage::getName, query.getKeywords())
                .orderByDesc(FileManage::getId);
        return page(page, queryWrapper);
    }

    @Transactional(rollbackFor = Throwable.class)
    public FileManageDto saveFileManage(MultipartFile file, Long userId, String platform) throws IOException {
        long fileSize = file.getSize();
        String fileName = file.getOriginalFilename();
        String suffix = FileUtil.getSuffix(fileName);
        // InputStream会被读取流，不可和存储共用一个流
        String md5 = SecureUtil.md5(file.getInputStream()).toUpperCase();
        // 检查md5是否存在，存在则无需再上传

        String url = storageService.store(file, md5, suffix);

        // 文件信息入库管理
        FileManageDto dto = new FileManageDto();
        dto.setName(fileName);
        dto.setSize(fileSize);
        dto.setPlatform(platform);
        dto.setMd5(md5);
        dto.setUrl(url);
        dto.setSuffix(suffix);
        dto.setUserId(userId);

        FileManage entity = converter.toEntity(dto);
        save(entity);
        return dto;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateFileManage(FileManageDto dto) {
        if (ObjectUtils.isEmpty(dto.getId())) {
            throw new BadParameterException();
        }
        FileManage entity = converter.toEntity(dto);
        updateById(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteByIds(List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            throw new BadParameterException();
        }
        removeByIds(ids);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteById(Long id) {
        removeById(id);
    }
}
