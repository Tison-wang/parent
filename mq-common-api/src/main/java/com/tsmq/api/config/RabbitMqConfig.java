package com.tsmq.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 *
 * @author
 * @version 1.0
 * @date 2020/3/29 21:55
 */
@Slf4j
@Configuration
public class RabbitMqConfig {

    public final static String DIRECT_EXCHANGE_NAME = "direct.exchange";

    public final static String TOPIC_EXCHANGE_NAME = "topic.exchange";

    public final static String FANOUT_EXCHANGE_NAME = "fanout.exchange";

    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.port}")
    private Integer port;
    @Value("${spring.rabbitmq.username}")
    private String userName;
    @Value("${spring.rabbitmq.password}")
    private String password;

    /**
     * Queue创建的name参数，即监听时的queue名称
     */
    @Bean
    public Queue queue1() {
        return new Queue("amqp.queue.test1");
    }

    @Bean
    public Queue queue2() {
        return new Queue("amqp.queue.test2");
    }

    @Bean
    public Queue queue3() {
        return new Queue("amqp.queue.test3");
    }

    @Bean
    public Queue queue4() {
        return new Queue("amqp.queue.test4");
    }

    @Bean
    public Queue queue5() {
        return new Queue("amqp.queue.test5");
    }

    /**
     * 定义direct方式的交换器
     * @Data:16:15 2020/3/29 21:55
     */
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(DIRECT_EXCHANGE_NAME);
    }

    /**
     * 定义主题方式的交换器
     * @Data:16:15 2020/3/29 21:55
     */
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }

    /**
     * 定义广播方式的交换器
     * @date 2020/3/30 15:55
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(FANOUT_EXCHANGE_NAME);
    }

    /**
     * 交换机与消息队列进行绑定 队列 messages 绑定交换机 hello
     * @Data:16:18 2020/3/29 21:55
     */
    @Bean
    public Binding bindingExchangeMessages1(@Qualifier("queue1") Queue queueMessages, DirectExchange directExchange){
        return BindingBuilder.bind(queueMessages).to(directExchange).with("hello1");
    }

    @Bean
    public Binding bindingExchangeMessages2(@Qualifier("queue2") Queue queueMessages, DirectExchange directExchange){
        return BindingBuilder.bind(queueMessages).to(directExchange).with("hello2");
    }

    @Bean
    public Binding bindingExchangeMessages3(@Qualifier("queue3") Queue queueMessages, TopicExchange topicExchange){
        return BindingBuilder.bind(queueMessages).to(topicExchange).with("#.topic.#");
    }

    @Bean
    public Binding bindingExchangeMessages4(@Qualifier("queue4") Queue queueMessages, FanoutExchange fanoutExchange){
        return BindingBuilder.bind(queueMessages).to(fanoutExchange);
    }

    @Bean
    public Binding bindingExchangeMessages5(@Qualifier("queue5") Queue queueMessages, FanoutExchange fanoutExchange){
        return BindingBuilder.bind(queueMessages).to(fanoutExchange);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setPrefetchCount(0);
        return factory;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory =  new CachingConnectionFactory(host, port);
        cachingConnectionFactory.setUsername(userName);
        cachingConnectionFactory.setPassword(password);
        return cachingConnectionFactory;
    }

}
