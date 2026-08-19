package com.harvey.screen.service;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.common.exception.BusinessException;
import com.harvey.common.utils.StringUtils;
import com.harvey.screen.api.ScreenCommandCode;
import com.harvey.screen.api.ScreenMessage;
import com.harvey.screen.cluster.ScreenClusterSessionRegistry;
import com.harvey.screen.mapper.ScreenCommandMapper;
import com.harvey.screen.mapper.ScreenDeviceMapper;
import com.harvey.screen.mapstruct.ScreenCommandConverter;
import com.harvey.screen.model.dto.ScreenCommandDto;
import com.harvey.screen.model.entity.ScreenCommand;
import com.harvey.screen.model.entity.ScreenDevice;
import com.harvey.screen.model.query.ScreenCommandQuery;
import com.harvey.screen.model.vo.ScreenCommandSendVO;
import com.harvey.screen.model.vo.ScreenCommandVO;
import com.harvey.screen.tcp.support.ScreenCommandSender;
import com.harvey.screen.tcp.support.ScreenSeqGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 信发指令 服务实现类
 *
 * @author Harvey
 */
@Service
@RequiredArgsConstructor
public class ScreenCommandService extends ServiceImpl<ScreenCommandMapper, ScreenCommand> {

    /** 指令状态 */
    public static final int STATUS_PENDING = 0;
    public static final int STATUS_SENT = 1;
    public static final int STATUS_EXECUTED = 2;
    public static final int STATUS_FAILED = 3;
    public static final int STATUS_TIMEOUT = 4;

    private final ScreenCommandMapper mapper;
    private final ScreenDeviceMapper deviceMapper;
    private final ScreenCommandConverter converter;
    private final ScreenCommandSender commandSender;
    private final ScreenSeqGenerator seqGenerator;
    private final ScreenClusterSessionRegistry clusterRegistry;

    /**
     * 分页查询指令记录
     */
    public Page<ScreenCommandVO> queryPage(ScreenCommandQuery query) {
        Page<ScreenCommand> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<ScreenCommand> queryWrapper = new LambdaQueryWrapper<ScreenCommand>()
                .like(StringUtils.isNotBlank(query.getKeywords()), ScreenCommand::getDeviceNo, query.getKeywords())
                .eq(StringUtils.isNotBlank(query.getDeviceNo()), ScreenCommand::getDeviceNo, query.getDeviceNo())
                .eq(query.getCmdCode() != null, ScreenCommand::getCmdCode, query.getCmdCode())
                .eq(query.getStatus() != null, ScreenCommand::getStatus, query.getStatus())
                .orderByDesc(ScreenCommand::getId);
        return converter.toPage(this.page(page, queryWrapper));
    }

    /**
     * 下发指令：入库 -> 经 TCP 长连接发送，根据是否在线流转状态
     */
    @Transactional(rollbackFor = Throwable.class)
    public ScreenCommandSendVO sendCommand(ScreenCommandDto dto) {
        ScreenDevice device = deviceMapper.selectOne(new LambdaQueryWrapper<ScreenDevice>()
                .eq(ScreenDevice::getDeviceNo, dto.getDeviceNo())
                .last("limit 1"));
        if (device == null) {
            throw new BusinessException("设备不存在: " + dto.getDeviceNo());
        }
        if (device.getEnabled() == null || device.getEnabled() != 1) {
            throw new BusinessException("设备已禁用: " + dto.getDeviceNo());
        }

        long seq = seqGenerator.next();
        Map<String, Object> params = dto.getParams();
        ScreenCommand entity = new ScreenCommand();
        entity.setDeviceId(device.getId());
        entity.setDeviceNo(dto.getDeviceNo());
        entity.setCmdCode(dto.getCmdCode());
        entity.setCmdName(resolveCmdName(dto.getCmdCode()));
        entity.setParams(JSONUtil.toJsonStr(params));
        entity.setSeq(seq);
        entity.setStatus(STATUS_PENDING);
        this.save(entity);

        boolean sent = commandSender.send(device.getDeviceNo(), ScreenMessage.command(seq, dto.getCmdCode(), params));
        if (sent) {
            entity.setStatus(STATUS_SENT);
            entity.setSendTime(LocalDateTime.now());
        } else if (clusterRegistry.forwardCommand(dto.getDeviceNo(), seq, dto.getCmdCode(), params)) {
            entity.setStatus(STATUS_SENT);
            entity.setSendTime(LocalDateTime.now());
            entity.setAckMessage("已转发至集群节点下发");
        } else {
            entity.setStatus(STATUS_FAILED);
            entity.setAckMessage("设备离线");
        }
        this.updateById(entity);
        String message = switch (entity.getStatus()) {
            case STATUS_SENT -> "指令已发送";
            case STATUS_FAILED -> "设备离线，发送失败";
            default -> "指令待发送";
        };
        return new ScreenCommandSendVO(entity.getId(), seq, entity.getStatus(), message);
    }

    /**
     * 指令编码 -> 名称
     */
    public static String resolveCmdName(int cmdCode) {
        return switch (cmdCode) {
            case ScreenCommandCode.POWER_ON -> "开机";
            case ScreenCommandCode.POWER_OFF -> "关机";
            case ScreenCommandCode.REBOOT -> "重启";
            case ScreenCommandCode.VOLUME_UP -> "音量加";
            case ScreenCommandCode.VOLUME_DOWN -> "音量减";
            case ScreenCommandCode.SCREENSHOT -> "截屏";
            case ScreenCommandCode.MARQUEE -> "滚动字幕";
            case ScreenCommandCode.MEDIA_PUSH -> "媒体推送";
            case ScreenCommandCode.QUERY_STATUS -> "查询状态";
            default -> "指令(" + cmdCode + ")";
        };
    }
}
