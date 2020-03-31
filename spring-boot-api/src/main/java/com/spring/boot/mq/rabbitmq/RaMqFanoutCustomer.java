package com.spring.boot.mq.rabbitmq;

import com.tsmq.api.dto.ObjectEntity;
import com.tsmq.api.utils.ObjectByteConvert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 *
 *
 * @author
 * @version 1.0
 * @date 2020/3/29 22:00
 */
@Slf4j
@Component
public class RaMqFanoutCustomer {

    @RabbitListener(queues = "amqp.queue.test4", containerFactory = "rabbitListenerContainerFactory")
    public void process1(Object hello) {
        ObjectEntity obj = ObjectByteConvert.toObject(((Message)hello).getBody());
        log.info("[rabbitmq消息已经消费-fanoutExchange4]: {}", obj);
    }

    @RabbitListener(queues = "amqp.queue.test5", containerFactory = "rabbitListenerContainerFactory")
    public void process2(Object hello) {
        ObjectEntity obj = ObjectByteConvert.toObject(((Message)hello).getBody());
        log.info("[rabbitmq消息已经消费-fanoutExchange5]: {}", obj);
    }

}
