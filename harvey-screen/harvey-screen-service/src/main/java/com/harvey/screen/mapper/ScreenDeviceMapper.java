package com.harvey.screen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.harvey.screen.model.entity.ScreenDevice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 信发设备 Mapper
 *
 * @author Harvey
 */
@Mapper
public interface ScreenDeviceMapper extends BaseMapper<ScreenDevice> {

    /**
     * 按设备编号查询，包含逻辑已删除记录（自定义 SQL 不受逻辑删除拦截，用于设备重连复活）
     */
    @Select("SELECT * FROM screen_device WHERE device_no = #{deviceNo} LIMIT 1")
    ScreenDevice selectByDeviceNoIncludingDeleted(@Param("deviceNo") String deviceNo);

    /**
     * 复活逻辑已删除的设备为待确认状态
     */
    @Update("""
            UPDATE screen_device
            SET deleted = 1, device_name = #{deviceNo}, model = #{model}, secret = #{secret},
                ip = #{ip}, status = 1, last_online_time = #{lastOnlineTime},
                audit_status = 0, enabled = 1, update_time = #{updateTime}
            WHERE device_no = #{deviceNo}
            """)
    int restoreDeleted(ScreenDevice device);

    /**
     * 分页查询已逻辑删除的设备(deleted=0)，旁路逻辑删除拦截
     */
    @Select("""
            <script>
            SELECT * FROM screen_device
            WHERE deleted = 0
            <if test="keywords != null and keywords != ''">
              AND (device_no LIKE CONCAT('%', #{keywords}, '%') OR device_name LIKE CONCAT('%', #{keywords}, '%'))
            </if>
            ORDER BY id DESC
            </script>
            """)
    IPage<ScreenDevice> selectDeletedPage(IPage<ScreenDevice> page, @Param("keywords") String keywords);
}
