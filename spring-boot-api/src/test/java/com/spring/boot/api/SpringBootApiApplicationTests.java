package com.spring.boot.api;

import com.base.common.utils.ObjectByteConvert;
import com.tsmq.api.dto.ObjectEntity;
import com.tsmq.api.producer.AcMqProducer;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootApiApplicationTests {

    @Autowired // 也可以注入JmsTemplate，JmsMessagingTemplate对JmsTemplate进行了封装
    private AcMqProducer acMqProducer;

    @Test
    public void contextLoads() {
        ObjectEntity obj = ObjectEntity.builder().userName("Lucy").age(28).build();
        acMqProducer.sendMessage(new ActiveMQQueue("activemq.test.queue"), ObjectByteConvert.toByteArray(obj));
    }

}
