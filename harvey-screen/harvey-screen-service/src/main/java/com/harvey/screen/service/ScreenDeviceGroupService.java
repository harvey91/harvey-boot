package com.harvey.screen.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.common.exception.BadParameterException;
import com.harvey.common.exception.BusinessException;
import com.harvey.common.utils.StringUtils;
import com.harvey.screen.api.ScreenCommandCode;
import com.harvey.screen.mapper.ScreenDeviceGroupMapper;
import com.harvey.screen.mapstruct.ScreenDeviceGroupConverter;
import com.harvey.screen.model.dto.ScreenCommandDto;
import com.harvey.screen.model.dto.ScreenDeviceGroupDto;
import com.harvey.screen.model.dto.ScreenGroupSendDto;
import com.harvey.screen.model.entity.ScreenDevice;
import com.harvey.screen.model.entity.ScreenDeviceGroup;
import com.harvey.screen.model.query.ScreenDeviceGroupQuery;
import com.harvey.screen.model.vo.ScreenCommandSendVO;
import com.harvey.screen.model.vo.ScreenDeviceGroupVO;
import com.harvey.screen.model.vo.ScreenGroupSendVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 信发设备分组 服务实现类
 *
 * @author Harvey
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScreenDeviceGroupService extends ServiceImpl<ScreenDeviceGroupMapper, ScreenDeviceGroup> {

    private static final String TYPE_COMMAND = "command";
    private static final String TYPE_MARQUEE = "marquee";
    private static final String TYPE_MEDIA = "media";

    private final ScreenDeviceGroupMapper mapper;
    private final ScreenDeviceGroupConverter converter;
    private final ScreenDeviceService deviceService;
    private final ScreenCommandService commandService;
    private final ScreenMarqueeService marqueeService;
    private final ScreenMediaService mediaService;

    /**
     * id查询表单
     */
    public ScreenDeviceGroupVO getFormById(Long id) {
        return converter.toVO(getById(id));
    }

    /**
     * 分页查询设备分组(含各分组设备数量)
     */
    public Page<ScreenDeviceGroupVO> queryPage(ScreenDeviceGroupQuery query) {
        Page<ScreenDeviceGroup> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<ScreenDeviceGroup> queryWrapper = new LambdaQueryWrapper<ScreenDeviceGroup>()
                .like(StringUtils.isNotBlank(query.getKeywords()), ScreenDeviceGroup::getGroupName, query.getKeywords())
                .orderByAsc(ScreenDeviceGroup::getSort)
                .orderByDesc(ScreenDeviceGroup::getId);
        Page<ScreenDeviceGroupVO> voPage = converter.toPage(this.page(page, queryWrapper));
        fillDeviceCount(voPage.getRecords());
        return voPage;
    }

    /**
     * 查询全部启用分组(下拉选择)
     */
    public List<ScreenDeviceGroupVO> listAll() {
        List<ScreenDeviceGroup> list = this.lambdaQuery()
                .eq(ScreenDeviceGroup::getEnabled, 1)
                .orderByAsc(ScreenDeviceGroup::getSort)
                .list();
        List<ScreenDeviceGroupVO> voList = list.stream().map(converter::toVO).toList();
        fillDeviceCount(voList);
        return voList;
    }

    /**
     * 填充各分组设备数量
     */
    private void fillDeviceCount(List<ScreenDeviceGroupVO> voList) {
        if (voList == null || voList.isEmpty()) {
            return;
        }
        Map<Long, Long> countMap = new HashMap<>();
        List<Map<String, Object>> rows = mapper.selectGroupDeviceCounts();
        for (Map<String, Object> row : rows) {
            Object gid = row.get("groupId");
            Object cnt = row.get("deviceCount");
            if (gid != null && cnt != null) {
                countMap.put(((Number) gid).longValue(), ((Number) cnt).longValue());
            }
        }
        voList.forEach(vo -> vo.setDeviceCount(countMap.getOrDefault(vo.getId(), 0L)));
    }

    @Transactional(rollbackFor = Throwable.class)
    public void create(ScreenDeviceGroupDto dto) {
        ScreenDeviceGroup entity = converter.toEntity(dto);
        this.save(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void modify(ScreenDeviceGroupDto dto) {
        if (ObjectUtils.isEmpty(dto.getId())) {
            throw new BadParameterException();
        }
        ScreenDeviceGroup entity = converter.toEntity(dto);
        this.updateById(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteById(Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BadParameterException();
        }
        this.removeById(id);
        deviceService.lambdaUpdate()
                .eq(ScreenDevice::getGroupId, id)
                .set(ScreenDevice::getGroupId, 0)
                .update();
    }

    /**
     * 分组批量下发：向分组内所有已审核且启用的设备下发指令/字幕/媒体
     */
    public ScreenGroupSendVO sendToGroup(ScreenGroupSendDto dto) {
        ScreenDeviceGroup group = getById(dto.getGroupId());
        if (group == null) {
            throw new BusinessException("设备分组不存在: " + dto.getGroupId());
        }
        List<ScreenDevice> devices = deviceService.lambdaQuery()
                .eq(ScreenDevice::getGroupId, dto.getGroupId())
                .eq(ScreenDevice::getEnabled, 1)
                .eq(ScreenDevice::getAuditStatus, 1)
                .orderByAsc(ScreenDevice::getSort)
                .list();
        if (devices.isEmpty()) {
            throw new BusinessException("分组内无已审核且启用的设备");
        }
        Integer cmdCode = resolveCmdCode(dto);
        Map<String, Object> params = resolveParams(dto);
        int success = 0;
        int failed = 0;
        for (ScreenDevice device : devices) {
            ScreenCommandDto command = new ScreenCommandDto();
            command.setDeviceNo(device.getDeviceNo());
            command.setCmdCode(cmdCode);
            command.setParams(params);
            try {
                ScreenCommandSendVO result = commandService.sendCommand(command);
                if (result.getStatus() != null && result.getStatus() == 3) {
                    failed++;
                } else {
                    success++;
                }
            } catch (Exception e) {
                failed++;
                log.warn("分组下发失败: group={}, deviceNo={}, cmdCode={}, err={}",
                        dto.getGroupId(), device.getDeviceNo(), cmdCode, e.getMessage());
            }
        }
        log.info("分组下发完成: group={}, type={}, total={}, success={}, failed={}",
                dto.getGroupId(), dto.getSendType(), devices.size(), success, failed);
        return new ScreenGroupSendVO(devices.size(), success, failed);
    }

    /**
     * 解析下发指令编码
     */
    private Integer resolveCmdCode(ScreenGroupSendDto dto) {
        return switch (dto.getSendType()) {
            case TYPE_MARQUEE -> ScreenCommandCode.MARQUEE;
            case TYPE_MEDIA -> ScreenCommandCode.MEDIA_PUSH;
            default -> {
                if (dto.getCmdCode() == null) {
                    throw new BusinessException("下发类型为指令时指令编码不能为空");
                }
                yield dto.getCmdCode();
            }
        };
    }

    /**
     * 解析下发指令参数
     */
    private Map<String, Object> resolveParams(ScreenGroupSendDto dto) {
        return switch (dto.getSendType()) {
            case TYPE_MARQUEE -> marqueeService.buildMarqueeParams(dto.getMarqueeId());
            case TYPE_MEDIA -> mediaService.buildMediaParams(dto.getMediaId());
            default -> dto.getParams() == null ? new HashMap<>() : dto.getParams();
        };
    }
}