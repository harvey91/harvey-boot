package com.harvey.screen.tcp.support;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 报文序号生成器(指令追踪/应答关联)
 *
 * @author Harvey
 */
@Component
public class ScreenSeqGenerator {

    private final AtomicLong seq = new AtomicLong(0);

    public long next() {
        return seq.incrementAndGet();
    }
}