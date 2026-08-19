package com.harvey.screen.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.common.exception.BusinessException;
import com.harvey.common.utils.StringUtils;
import com.harvey.screen.mapper.ScreenDeviceGroupMapper;
import com.harvey.screen.mapper.ScreenDeviceMapper;
import com.harvey.screen.mapstruct.ScreenDeviceConverter;
import com.harvey.screen.model.dto.ScreenDeviceDto;
import com.harvey.screen.model.entity.ScreenDevice;
import com.harvey.screen.model.entity.ScreenDeviceGroup;
import com.harvey.screen.model.query.ScreenDeviceQuery;
import com.harvey.screen.model.vo.ScreenDeviceVO;
import com.harvey.screen.cluster.ScreenClusterSessionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 信发设备 服务实现类
 *
 * @author Harvey
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScreenDeviceService extends ServiceImpl<ScreenDeviceMapper, ScreenDevice> {

    private final ScreenDeviceMapper mapper;
    private final ScreenDeviceConverter converter;
    private final ScreenClusterSessionRegistry clusterRegistry;
    private final ScreenDeviceGroupMapper deviceGroupMapper;

    /**
     * 分页查询设备(合并 TCP 实时在线状态)
     */
    public Page<ScreenDeviceVO> queryPage(ScreenDeviceQuery query) {
        if (query.getDeleted() != null && query.getDeleted() == 1) {
            return queryDeletedPage(query);
        }
        Page<ScreenDevice> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<ScreenDevice> queryWrapper = new LambdaQueryWrapper<ScreenDevice>()
                .and(StringUtils.isNotBlank(query.getKeywords()),
                        w -> w.like(ScreenDevice::getDeviceNo, query.getKeywords())
                                .or()
                                .like(ScreenDevice::getDeviceName, query.getKeywords()))
                .eq(query.getStatus() != null, ScreenDevice::getStatus, query.getStatus())
                .eq(query.getAuditStatus() != null, ScreenDevice::getAuditStatus, query.getAuditStatus())
                .eq(query.getGroupId() != null, ScreenDevice::getGroupId, query.getGroupId())
                .orderByDesc(ScreenDevice::getId);
        Page<ScreenDeviceVO> voPage = converter.toPage(this.page(page, queryWrapper));
        fillGroupName(voPage.getRecords());
        voPage.getRecords().forEach(vo -> vo.setOnline(clusterRegistry.isOnlineGlobal(vo.getDeviceNo())));
        return voPage;
    }

    /**
     * 分页查询已删除设备(旁路逻辑删除拦截)
     */
    private Page<ScreenDeviceVO> queryDeletedPage(ScreenDeviceQuery query) {
        Page<ScreenDevice> page = new Page<>(query.getPageNum(), query.getPageSize());
        mapper.selectDeletedPage(page, query.getKeywords());
        Page<ScreenDeviceVO> voPage = converter.toPage(page);
        fillGroupName(voPage.getRecords());
        voPage.getRecords().forEach(vo -> vo.setOnline(clusterRegistry.isOnlineGlobal(vo.getDeviceNo())));
        return voPage;
    }

    /**
     * 批量填充设备分组名称
     */
    private void fillGroupName(List<ScreenDeviceVO> voList) {
        if (voList == null || voList.isEmpty()) {
            return;
        }
        Set<Long> groupIds = voList.stream()
                .map(ScreenDeviceVO::getGroupId)
                .filter(Objects::nonNull)
                .filter(id -> id > 0)
                .collect(Collectors.toSet());
        if (groupIds.isEmpty()) {
            return;
        }
        List<ScreenDeviceGroup> groups = deviceGroupMapper.selectBatchIds(groupIds);
        Map<Long, String> nameMap = groups.stream()
                .collect(Collectors.toMap(ScreenDeviceGroup::getId, ScreenDeviceGroup::getGroupName));
        voList.forEach(vo -> vo.setGroupName(nameMap.get(vo.getGroupId())));
    }

    /**
     * 查询所有已审核且启用的设备(供分组下发/下拉选择)
     */
    public List<ScreenDeviceVO> listEnabled() {
        List<ScreenDevice> list = this.lambdaQuery()
                .eq(ScreenDevice::getEnabled, 1)
                .eq(ScreenDevice::getAuditStatus, 1)
                .orderByAsc(ScreenDevice::getSort)
                .list();
        List<ScreenDeviceVO> voList = list.stream().map(converter::toVO).toList();
        fillGroupName(voList);
        voList.forEach(vo -> vo.setOnline(clusterRegistry.isOnlineGlobal(vo.getDeviceNo())));
        return voList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void saveDevice(ScreenDeviceDto dto) {
        long count = this.lambdaQuery().eq(ScreenDevice::getDeviceNo, dto.getDeviceNo()).count();
        if (count > 0) {
            throw new BusinessException("设备编号已存在");
        }
        ScreenDevice entity = converter.toEntity(dto);
        if (entity.getStatus() == null) {
            entity.setStatus(0);
        }
        if (entity.getAuditStatus() == null) {
            entity.setAuditStatus(1);
        }
        this.save(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateDevice(ScreenDeviceDto dto) {
        ScreenDevice entity = this.getById(dto.getId());
        if (entity == null) {
            throw new BusinessException("设备不存在");
        }
        entity.setDeviceNo(dto.getDeviceNo());
        entity.setDeviceName(dto.getDeviceName());
        entity.setModel(dto.getModel());
        entity.setGroupId(dto.getGroupId());
        entity.setSecret(dto.getSecret());
        entity.setRemark(dto.getRemark());
        entity.setEnabled(dto.getEnabled());
        this.updateById(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteByIds(List<Long> ids) {
        this.removeByIds(ids);
    }

    /**
     * 审核通过设备(待确认 -> 已通过并启用)
     */
    @Transactional(rollbackFor = Throwable.class)
    public void approve(Long id) {
        ScreenDevice entity = this.getById(id);
        if (entity == null) {
            throw new BusinessException("设备不存在");
        }
        ScreenDevice update = new ScreenDevice();
        update.setId(id);
        update.setAuditStatus(1);
        update.setEnabled(1);
        this.updateById(update);
        log.info("设备审核通过: id={}, deviceNo={}", id, entity.getDeviceNo());
    }

    /**
     * 审核拒绝设备(待确认 -> 已拒绝并禁用)
     */
    @Transactional(rollbackFor = Throwable.class)
    public void reject(Long id) {
        ScreenDevice entity = this.getById(id);
        if (entity == null) {
            throw new BusinessException("设备不存在");
        }
        ScreenDevice update = new ScreenDevice();
        update.setId(id);
        update.setAuditStatus(2);
        update.setEnabled(0);
        this.updateById(update);
        log.info("设备审核拒绝: id={}, deviceNo={}", id, entity.getDeviceNo());
    }

    /**
     * 当前在线设备数(TCP 会话实时数量)
     */
    public int onlineCount() {
        return clusterRegistry.onlineCountGlobal();
    }
}
