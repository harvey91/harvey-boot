package com.harvey.screen.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.common.exception.BusinessException;
import com.harvey.common.utils.StringUtils;
import com.harvey.core.storage.StorageService;
import com.harvey.screen.api.ScreenCommandCode;
import com.harvey.screen.mapper.ScreenMediaMapper;
import com.harvey.screen.mapstruct.ScreenMediaConverter;
import com.harvey.screen.model.dto.ScreenCommandDto;
import com.harvey.screen.model.dto.ScreenMediaPushDto;
import com.harvey.screen.model.entity.ScreenMedia;
import com.harvey.screen.model.query.ScreenMediaQuery;
import com.harvey.screen.model.vo.ScreenMediaVO;
import com.harvey.screen.model.vo.ScreenCommandSendVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 信发媒体库 服务实现类
 *
 * @author Harvey
 */
@Service
@RequiredArgsConstructor
public class ScreenMediaService extends ServiceImpl<ScreenMediaMapper, ScreenMedia> {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private final ScreenMediaMapper mapper;
    private final ScreenMediaConverter converter;
    private final StorageService storageService;
    private final ScreenCommandService commandService;

    /**
     * 分页查询媒体库
     */
    public Page<ScreenMediaVO> queryPage(ScreenMediaQuery query) {
        Page<ScreenMedia> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<ScreenMedia> queryWrapper = new LambdaQueryWrapper<ScreenMedia>()
                .like(StringUtils.isNotBlank(query.getKeywords()), ScreenMedia::getMediaName, query.getKeywords())
                .eq(query.getMediaType() != null, ScreenMedia::getMediaType, query.getMediaType())
                .orderByDesc(ScreenMedia::getId);
        return converter.toPage(this.page(page, queryWrapper));
    }

    /**
     * 上传媒体：写入存储并登记媒体库(按 md5 去重)
     */
    @Transactional(rollbackFor = Throwable.class)
    public ScreenMediaVO upload(MultipartFile file, Integer mediaType) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }
        if (mediaType == null || (mediaType != MEDIA_TYPE_IMAGE && mediaType != MEDIA_TYPE_VIDEO)) {
            throw new BusinessException("媒体类型不正确(1图片 2视频)");
        }
        String fileName = file.getOriginalFilename();
        String suffix = FileUtil.getSuffix(fileName);
        String md5 = SecureUtil.md5(file.getInputStream()).toUpperCase();

        ScreenMedia exist = mapper.selectOne(new LambdaQueryWrapper<ScreenMedia>()
                .eq(ScreenMedia::getMd5, md5)
                .eq(ScreenMedia::getMediaType, mediaType)
                .last("limit 1"));
        if (exist != null) {
            return converter.toVO(exist);
        }

        String url = storageService.store(file, md5, suffix);

        ScreenMedia entity = new ScreenMedia();
        entity.setMediaName(fileName);
        entity.setMediaType(mediaType);
        entity.setUrl(url);
        entity.setMd5(md5);
        entity.setSize(file.getSize());
        entity.setSuffix(suffix);
        this.save(entity);
        return converter.toVO(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteById(Long id) {
        ScreenMedia media = getById(id);
        if (media == null) {
            throw new BusinessException("媒体不存在");
        }
        removeById(id);
    }

    /**
     * 推送媒体到设备：下发 MEDIA_PUSH 指令，设备端按 url 拉取播放
     */
    public ScreenCommandSendVO push(ScreenMediaPushDto dto) {
        ScreenMedia media = getById(dto.getMediaId());
        if (media == null) {
            throw new BusinessException("媒体不存在: " + dto.getMediaId());
        }
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("mediaId", media.getId());
        params.put("mediaName", media.getMediaName());
        params.put("mediaType", media.getMediaType());
        params.put("url", media.getUrl());
        params.put("md5", media.getMd5());
        params.put("size", media.getSize());
        ScreenCommandDto command = new ScreenCommandDto();
        command.setDeviceNo(dto.getDeviceNo());
        command.setCmdCode(ScreenCommandCode.MEDIA_PUSH);
        command.setParams(params);
        return commandService.sendCommand(command);
    }
}