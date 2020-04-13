package com.tsmq.api.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 *
 * @author
 * @version 1.0
 * @date 2020/3/27 16:01
 */
@Slf4j
@EnableJms
@Configuration
@PropertySource("classpath:config.properties")
public class ActiveMqConfig {

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    private static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("activemq-pool-%d").setDaemon(true).build();

    private static ExecutorService pool;

    static {
        if (pool == null) {
            pool = new ThreadPoolExecutor(6, 6,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(6), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        }
    }

    public ConnectionFactory connectionFactory(){
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(brokerUrl);
        connectionFactory.setUserName("admin");
        connectionFactory.setPassword("admin");
        return connectionFactory;
    }

    @Bean
    public Connection activeConnection() throws JMSException {
        try {
            Connection connection = this.connectionFactory().createConnection();
            return connection;
        } catch (JMSException e) {
            log.error("[activeMq 服务异常，请查检！]，原因：{}", e.getMessage());
        }
        return null;
    }

    /**
     * 处理topic消息
     *
     * @return
     */
    @Bean("topicListenerFactory")
    public JmsListenerContainerFactory<?> topicListenerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setPubSubDomain(true);
        factory.setConnectionFactory(connectionFactory());
        factory.setTaskExecutor(pool);
        return factory;
    }

    /**
     * 处理queue消息
     *
     * @return
     */
    @Bean("queueListenerFactory")
    public JmsListenerContainerFactory<?> queueListenerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setPubSubDomain(false);
        factory.setConnectionFactory(connectionFactory());
        factory.setTaskExecutor(pool);
        return factory;
    }

}
