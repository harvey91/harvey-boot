package com.harvey.screen.listener;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.harvey.screen.cluster.ScreenClusterSessionRegistry;
import com.harvey.screen.mapper.ScreenDeviceMapper;
import com.harvey.screen.model.entity.ScreenDevice;
import com.harvey.screen.tcp.listener.ScreenSessionListener;
import com.harvey.screen.tcp.session.ScreenSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 设备会话监听器：设备上下线时持久化在线状态/最后在线时间/IP
 *
 * @author Harvey
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ScreenDeviceSessionListener implements ScreenSessionListener {

    private final ScreenDeviceMapper deviceMapper;
    private final ScreenClusterSessionRegistry clusterRegistry;

    @Override
    public void onLogin(ScreenSession session) {
        ScreenDevice device = findByDeviceNo(session.getDeviceNo());
        if (device == null) {
            ScreenDevice created = new ScreenDevice();
            created.setDeviceNo(session.getDeviceNo());
            created.setDeviceName(session.getDeviceNo());
            created.setModel(session.getModel());
            created.setSecret(session.getToken());
            created.setIp(session.getIp());
            created.setStatus(1);
            created.setLastOnlineTime(LocalDateTime.now());
            created.setEnabled(1);
            created.setAuditStatus(0);
            deviceMapper.insert(created);
            session.setAuditStatus(0);
            log.info("设备首次连接自动注册(待确认): deviceNo={}, model={}, ip={}",
                    session.getDeviceNo(), session.getModel(), session.getIp());
            return;
        }
        session.setAuditStatus(device.getAuditStatus());
        ScreenDevice update = new ScreenDevice();
        update.setId(device.getId());
        update.setStatus(1);
        update.setIp(session.getIp());
        update.setLastOnlineTime(LocalDateTime.now());
        deviceMapper.updateById(update);
        log.info("设备上线状态已更新: deviceNo={}, ip={}", session.getDeviceNo(), session.getIp());
    }

    @Override
    public void onLogout(ScreenSession session) {
        if (clusterRegistry.isOnlineGlobal(session.getDeviceNo())) {
            log.info("设备在其他节点仍在线, 跳过下线状态更新: deviceNo={}", session.getDeviceNo());
            return;
        }
        ScreenDevice device = findByDeviceNo(session.getDeviceNo());
        if (device == null) {
            return;
        }
        ScreenDevice update = new ScreenDevice();
        update.setId(device.getId());
        update.setStatus(0);
        deviceMapper.updateById(update);
        log.info("设备下线状态已更新: deviceNo={}", session.getDeviceNo());
    }

    private ScreenDevice findByDeviceNo(String deviceNo) {
        return deviceMapper.selectOne(new LambdaQueryWrapper<ScreenDevice>()
                .eq(ScreenDevice::getDeviceNo, deviceNo)
                .last("limit 1"));
    }
}
