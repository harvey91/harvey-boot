package com.harvey.screen.listener;

import com.harvey.screen.api.AckMessage;
import com.harvey.screen.mapper.ScreenCommandMapper;
import com.harvey.screen.model.entity.ScreenCommand;
import com.harvey.screen.service.ScreenCommandService;
import com.harvey.screen.tcp.listener.ScreenAckListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 指令 ACK 监听器：设备应答后，按(设备编号, 报文序号)将指令状态流转为"已执行"
 *
 * @author Harvey
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ScreenCommandAckListener implements ScreenAckListener {

    private final ScreenCommandMapper commandMapper;

    @Override
    public void onAck(String deviceNo, AckMessage ack) {
        ScreenCommand command = commandMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ScreenCommand>()
                        .eq(ScreenCommand::getDeviceNo, deviceNo)
                        .eq(ScreenCommand::getSeq, ack.getAckSeq())
                        .last("limit 1"));
        if (command == null) {
            return;
        }
        ScreenCommand update = new ScreenCommand();
        update.setId(command.getId());
        update.setStatus(ScreenCommandService.STATUS_EXECUTED);
        update.setAckTime(LocalDateTime.now());
        update.setAckMessage(ack.getMessage());
        commandMapper.updateById(update);
        log.info("指令已执行: commandId={}, deviceNo={}, seq={}, ackMsg={}", command.getId(), deviceNo, ack.getAckSeq(), ack.getMessage());
    }
}
