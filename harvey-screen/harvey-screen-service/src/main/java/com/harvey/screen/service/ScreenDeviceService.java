package com.harvey.screen.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.common.exception.BusinessException;
import com.harvey.common.utils.StringUtils;
import com.harvey.screen.mapper.ScreenDeviceMapper;
import com.harvey.screen.mapstruct.ScreenDeviceConverter;
import com.harvey.screen.model.dto.ScreenDeviceDto;
import com.harvey.screen.model.entity.ScreenDevice;
import com.harvey.screen.model.query.ScreenDeviceQuery;
import com.harvey.screen.model.vo.ScreenDeviceVO;
import com.harvey.screen.cluster.ScreenClusterSessionRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 信发设备 服务实现类
 *
 * @author Harvey
 */
@Service
@RequiredArgsConstructor
public class ScreenDeviceService extends ServiceImpl<ScreenDeviceMapper, ScreenDevice> {

    private final ScreenDeviceMapper mapper;
    private final ScreenDeviceConverter converter;
    private final ScreenClusterSessionRegistry clusterRegistry;

    /**
     * 分页查询设备(合并 TCP 实时在线状态)
     */
    public Page<ScreenDeviceVO> queryPage(ScreenDeviceQuery query) {
        Page<ScreenDevice> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<ScreenDevice> queryWrapper = new LambdaQueryWrapper<ScreenDevice>()
                .and(StringUtils.isNotBlank(query.getKeywords()),
                        w -> w.like(ScreenDevice::getDeviceNo, query.getKeywords())
                                .or()
                                .like(ScreenDevice::getDeviceName, query.getKeywords()))
                .eq(query.getStatus() != null, ScreenDevice::getStatus, query.getStatus())
                .orderByDesc(ScreenDevice::getId);
        Page<ScreenDeviceVO> voPage = converter.toPage(this.page(page, queryWrapper));
        voPage.getRecords().forEach(vo -> vo.setOnline(clusterRegistry.isOnlineGlobal(vo.getDeviceNo())));
        return voPage;
    }

    /**
     * 查询所有已启用设备(供分组下发/下拉选择)
     */
    public List<ScreenDeviceVO> listEnabled() {
        List<ScreenDevice> list = this.lambdaQuery()
                .eq(ScreenDevice::getEnabled, 1)
                .orderByAsc(ScreenDevice::getSort)
                .list();
        List<ScreenDeviceVO> voList = list.stream().map(converter::toVO).toList();
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
     * 当前在线设备数(TCP 会话实时数量)
     */
    public int onlineCount() {
        return clusterRegistry.onlineCountGlobal();
    }
}
