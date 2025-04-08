package com.harvey.system.task;

import org.springframework.stereotype.Component;

/**
 * 定时任务调度
 * @author Harvey
 * @since 2025-04-08 18:46
 **/
@Component("task")
public class Task {

    public void test() {
        System.out.println("测试定时任务调度-无参方法");
    }

    public void test(String s) {
        System.out.println("测试定时任务调度-有参方法");
    }
}
