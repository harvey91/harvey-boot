package com.harvey.screen.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.common.exception.BadParameterException;
import com.harvey.common.exception.BusinessException;
import com.harvey.common.utils.StringUtils;
import com.harvey.screen.api.ScreenCommandCode;
import com.harvey.screen.mapper.ScreenMarqueeMapper;
import com.harvey.screen.mapstruct.ScreenMarqueeConverter;
import com.harvey.screen.model.dto.ScreenCommandDto;
import com.harvey.screen.model.dto.ScreenMarqueeDto;
import com.harvey.screen.model.dto.ScreenMarqueeSendDto;
import com.harvey.screen.model.entity.ScreenMarquee;
import com.harvey.screen.model.query.ScreenMarqueeQuery;
import com.harvey.screen.model.vo.ScreenCommandSendVO;
import com.harvey.screen.model.vo.ScreenMarqueeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 信发滚动字幕模板 服务实现类
 *
 * @author Harvey
 */
@Service
@RequiredArgsConstructor
public class ScreenMarqueeService extends ServiceImpl<ScreenMarqueeMapper, ScreenMarquee> {

    private final ScreenMarqueeMapper mapper;
    private final ScreenMarqueeConverter converter;
    private final ScreenCommandService commandService;

    /**
     * id查询表单
     */
    public ScreenMarqueeVO getFormById(Long id) {
        return converter.toVO(getById(id));
    }

    /**
     * 分页查询字幕模板
     */
    public Page<ScreenMarqueeVO> queryPage(ScreenMarqueeQuery query) {
        Page<ScreenMarquee> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<ScreenMarquee> queryWrapper = new LambdaQueryWrapper<ScreenMarquee>()
                .like(StringUtils.isNotBlank(query.getKeywords()), ScreenMarquee::getTitle, query.getKeywords())
                .orderByDesc(ScreenMarquee::getId);
        return converter.toPage(this.page(page, queryWrapper));
    }

    @Transactional(rollbackFor = Throwable.class)
    public void create(ScreenMarqueeDto dto) {
        ScreenMarquee entity = converter.toEntity(dto);
        this.save(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void modify(ScreenMarqueeDto dto) {
        if (ObjectUtils.isEmpty(dto.getId())) {
            throw new BadParameterException();
        }
        ScreenMarquee entity = converter.toEntity(dto);
        this.updateById(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteById(Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BadParameterException();
        }
        removeById(id);
    }

    /**
     * 下发字幕模板到设备：构建 MARQUEE 指令参数并发送
     */
    public ScreenCommandSendVO send(ScreenMarqueeSendDto dto) {
        ScreenMarquee marquee = getById(dto.getMarqueeId());
        if (marquee == null) {
            throw new BusinessException("字幕模板不存在: " + dto.getMarqueeId());
        }
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("marqueeId", marquee.getId());
        params.put("title", marquee.getTitle());
        params.put("content", marquee.getContent());
        params.put("speed", marquee.getSpeed());
        params.put("color", marquee.getColor());
        params.put("fontSize", marquee.getFontSize());
        params.put("repeat", marquee.getRepeatCount());
        ScreenCommandDto command = new ScreenCommandDto();
        command.setDeviceNo(dto.getDeviceNo());
        command.setCmdCode(ScreenCommandCode.MARQUEE);
        command.setParams(params);
        return commandService.sendCommand(command);
    }
}