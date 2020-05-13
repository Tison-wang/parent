package com.spring.boot.mq.activemq;

import com.base.common.utils.ObjectByteConvert;
import com.tsmq.api.dto.ObjectEntity;
import com.tsmq.api.monitor.IMqQueueMonitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author
 * @version 1.0
 * @date 2020/3/27 15:01
 */
@Slf4j
@Component
public class AcMqQueueCustomer implements IMqQueueMonitor {

    @Override
    @JmsListener(destination = "${activemq.test.queue}", containerFactory = "queueListenerFactory")
    public void receiveQueue(byte[] value) {
        ObjectEntity obj = ObjectByteConvert.toObject(value);
        log.info("[activemq消息已经消费(queue)]：{}", obj);
    }

}
