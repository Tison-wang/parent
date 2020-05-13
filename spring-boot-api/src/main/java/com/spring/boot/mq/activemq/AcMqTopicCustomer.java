package com.spring.boot.mq.activemq;

import com.base.common.utils.ObjectByteConvert;
import com.tsmq.api.dto.ObjectEntity;
import com.tsmq.api.monitor.IMqTopicMonitor;
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
public class AcMqTopicCustomer implements IMqTopicMonitor {

    @Override
    @JmsListener(destination = "${activemq.test.topic}", containerFactory = "topicListenerFactory")
    public void receiveTopic(byte[] value) {
        ObjectEntity obj = ObjectByteConvert.toObject(value);
        log.info("[activemq消息已经消费(topic)]：{}", obj);
    }

}
