package com.tsmq.api.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;

/**
 *
 *
 * @author
 * @version 1.0
 * @date 2020/3/27 14:59
 */
@Component
public class AcMqProducer {

    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    // 发送消息，destination是发送到的队列，message是待发送的消息
    public void sendMessage(Destination destination, final byte[] message){
        jmsTemplate.convertAndSend(destination, message);
    }

}
