package com.tsmq.api.monitor;

/**
 *
 *
 * @author
 * @version 1.0
 * @date 2020/3/28 10:01
 */
public interface IMqTopicMonitor {

    /**
     * 执行对 Topic 队列消息的消费逻辑
     *
     * @author
     * @date 2020/3/28 10:06
     * @param value 主题中存储的字节数组
     * @return
     */
    void receiveTopic(byte[] value);

}
