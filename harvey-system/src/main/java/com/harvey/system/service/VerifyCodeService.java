package com.harvey.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.enums.ContactTypeEnum;
import com.harvey.system.model.query.VerifyCodeQuery;
import com.harvey.system.mapstruct.VerifyCodeConverter;
import com.harvey.system.model.dto.VerifyCodeDto;
import com.harvey.system.model.entity.VerifyCode;
import com.harvey.system.mapper.VerifyCodeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.system.utils.StringUtils;
import com.harvey.system.exception.BadParameterException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 验证码 服务实现类
 *
 * @author harvey
 * @since 2024-12-11
 */
@Service
@RequiredArgsConstructor
public class VerifyCodeService extends ServiceImpl<VerifyCodeMapper, VerifyCode> {
    private final VerifyCodeMapper mapper;
    private final VerifyCodeConverter converter;

    public Page<VerifyCode> queryPage(VerifyCodeQuery query) {
        Page<VerifyCode> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<VerifyCode> queryWrapper = new LambdaQueryWrapper<VerifyCode>()
//                .like(StringUtils.isNotBlank(query.getKeywords()), VerifyCode::getName, query.getKeywords())
                .orderByAsc(VerifyCode::getSort);
        return page(page, queryWrapper);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void saveVerifyCode(VerifyCodeDto dto) {
        VerifyCode entity = converter.toEntity(dto);
        save(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateVerifyCode(VerifyCodeDto dto) {
        if (ObjectUtils.isEmpty(dto.getId())) {
            throw new BadParameterException();
        }
        VerifyCode entity = converter.toEntity(dto);
        updateById(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteByIds(List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            throw new BadParameterException();
        }
        removeByIds(ids);
    }

    /**
     * 发送验证码
     * @param contact 联系方式
     * @param contactType 类型
     * @param platform 平台
     */
    @Transactional(rollbackFor = Throwable.class)
    public void sendCode(String contact, Integer contactType, Integer verifyType, Integer platform) {
        VerifyCode entity = new VerifyCode();
        entity.setContact(contact);
        entity.setVerifyCode("");
        entity.setContactType(contactType);
        entity.setVerifyType(verifyType);
        entity.setPlatform(platform);
        // 过期时间
        LocalDateTime expireTime = LocalDateTime.now();
        entity.setExpireTime(expireTime);
        this.save(entity);
    }
}
