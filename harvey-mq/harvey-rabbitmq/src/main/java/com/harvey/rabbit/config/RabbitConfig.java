package com.harvey.rabbit.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Harvey
 * @date 2025-03-12 11:35
 **/

@Configuration
public class RabbitConfig {

    /**
     * 队列
     * @return
     */
    @Bean
    public Queue myQueue() {
        // 队列名称：myQueue，持久化
        return new Queue("myQueue", true);
    }

    /**
     * 直连交换机
     * 精确匹配RoutingKey
     * @return
     */
    @Bean
    public DirectExchange myDirectExchange() {
        return new DirectExchange("myDirectExchange");
    }

    /**
     * 广播交换机
     * 忽略RoutingKey
     * @return
     */
//    @Bean
//    public FanoutExchange myFanoutExchange() {
//        return new FanoutExchange("myFanoutExchange");
//    }

    /**
     * 主题交换机
     * 通配符匹配RoutingKey
     * @return
     */
//    @Bean
//    public TopicExchange myTopicExchange() {
//        return new TopicExchange("myTopicExchange");
//    }

    /**
     * 头交换机
     * Header匹配规则，略RoutingKey
     * @return
     */
//    @Bean
//    public HeadersExchange myHeadersExchange() {
//        return new HeadersExchange("myHeadersExchange");
//    }

    /**
     * 死信交换机
     * rabbitmq服务端需要安装插件：rabbitmq_delayed_message_exchange
     * @return
     */
    /*@Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange("delayedExchange", "x-delayed-message", true, false, args);
    }*/

    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(myQueue())
                .to(myDirectExchange())
                .with("rk.002");
    }
}