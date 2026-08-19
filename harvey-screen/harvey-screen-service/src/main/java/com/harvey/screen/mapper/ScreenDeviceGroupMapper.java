package com.harvey.screen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harvey.screen.model.entity.ScreenDeviceGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 信发设备分组 Mapper
 *
 * @author Harvey
 */
@Mapper
public interface ScreenDeviceGroupMapper extends BaseMapper<ScreenDeviceGroup> {

    /**
     * 各分组内设备数量统计(已启用且已审核通过)
     */
    @Select("SELECT group_id AS groupId, COUNT(*) AS deviceCount FROM screen_device " +
            "WHERE deleted = 1 AND group_id > 0 AND enabled = 1 AND audit_status = 1 " +
            "GROUP BY group_id")
    List<Map<String, Object>> selectGroupDeviceCounts();
}