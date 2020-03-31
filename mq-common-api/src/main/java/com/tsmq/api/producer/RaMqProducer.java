package com.tsmq.api.producer;

import com.tsmq.api.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 *
 * @author
 * @version 1.0
 * @date 2020/3/29 21:57
 */
@Slf4j
@Component
public class RaMqProducer {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void sendMessage(Object value){
        this.rabbitTemplate.convertAndSend(RabbitMqConfig.DIRECT_EXCHANGE_NAME,"hello1", value);
        this.rabbitTemplate.convertAndSend(RabbitMqConfig.DIRECT_EXCHANGE_NAME,"hello2", value);
        this.rabbitTemplate.convertAndSend(RabbitMqConfig.TOPIC_EXCHANGE_NAME,"log.topic.info", value);
        /**
         * fanout方式，无需指定 binding_key，消息会被发送到绑定了 fanout_exchange 交换器的所有 Queue
         */
        this.rabbitTemplate.convertAndSend(RabbitMqConfig.FANOUT_EXCHANGE_NAME,"", value);
    }

}
