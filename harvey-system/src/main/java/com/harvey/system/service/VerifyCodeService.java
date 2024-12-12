package com.harvey.system.service;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.constant.CacheConstant;
import com.harvey.system.enums.ContactTypeEnum;
import com.harvey.system.enums.EnabledEnum;
import com.harvey.system.exception.BusinessException;
import com.harvey.system.model.query.VerifyCodeQuery;
import com.harvey.system.mapstruct.VerifyCodeConverter;
import com.harvey.system.model.dto.VerifyCodeDto;
import com.harvey.system.model.entity.VerifyCode;
import com.harvey.system.mapper.VerifyCodeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.system.redis.RedisCache;
import com.harvey.system.utils.StringUtils;
import com.harvey.system.exception.BadParameterException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private final RedisCache redisCache;

    public Page<VerifyCode> queryPage(VerifyCodeQuery query) {
        Page<VerifyCode> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<VerifyCode> queryWrapper = new LambdaQueryWrapper<VerifyCode>()
                .like(StringUtils.isNotBlank(query.getKeywords()), VerifyCode::getContact, query.getKeywords())
                .like(!ObjectUtils.isEmpty(query.getEnabled()), VerifyCode::getEnabled, query.getEnabled())
                .orderByDesc(VerifyCode::getId);
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
        String verifyCode = RandomUtil.randomNumbers(4);
        VerifyCode entity = new VerifyCode();
        entity.setContact(contact);
        entity.setContactType(contactType);
        entity.setVerifyCode(verifyCode);
        entity.setVerifyType(verifyType);
        entity.setPlatform(platform);
        // 过期时间 = 当前时间 + 5分钟
        int timeout = 5;
        LocalDateTime expireTime = LocalDateTimeUtil.offset(LocalDateTime.now(), timeout, ChronoUnit.MINUTES);
        entity.setExpireTime(expireTime);
        // 发送 TODO
        // 缓存
//        String key = getCacheKey(contact, verifyType);
//        redisCache.setEx(key, verifyCode, timeout, TimeUnit.MINUTES);
        // 入库
        this.save(entity);
    }

    /**
     * 获取缓存key
     * @param contact 联系方式，手机号/邮箱
     * @param verifyType 验证类型，登陆/绑定...
     * @return
     */
    public String getCacheKey(String contact, Integer verifyType) {
        return CacheConstant.LOGIN_CAPTCHA_KEY + verifyType + ":" + contact;
    }

    /**
     * 验证
     * @param contact
     * @param code
     * @param verifyType
     */
    public void verify(String contact, String code, Integer verifyType) {
        LambdaQueryWrapper<VerifyCode> queryWrapper = new LambdaQueryWrapper<VerifyCode>()
                .eq(VerifyCode::getContact, contact)
                .eq(VerifyCode::getVerifyType, verifyType)
                .eq(VerifyCode::getVerifyCode, code)
                .eq(VerifyCode::getEnabled, EnabledEnum.ENABLE.getValue());
        VerifyCode one = this.getOne(queryWrapper);
        if (ObjectUtils.isEmpty(one)) {
            throw new BusinessException("验证码错误");
        }
        if (!code.equals(one.getVerifyCode())) {
            throw new BusinessException("验证码错误");
        }
        VerifyCode entity = new VerifyCode();
        entity.setId(one.getId());
        entity.setEnabled(EnabledEnum.DISABLE.getValue());
        this.updateById(entity);
    }
}
