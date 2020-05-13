package com.tszk.common.api.config;

import com.tszk.common.api.client.ZookeeperClient;
import com.tszk.common.api.listener.CusZkSerializer;
import com.tszk.common.api.route.ZuulRoute;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * ZookeeperConfig配置类
 *
 * @author
 * @version 1.0
 * @date 2020/3/9 17:30
 */
@Slf4j
@Configuration
public class ZookeeperConfig {

    @Value("${zookeeper.address}")
    private String connectString;

    @Value("${zookeeper.timeout}")
    private int timeout;

    @Bean(name = "zkClient2")
    public ZkClient zkClient2() {
        ZkClient zk = new ZkClient(connectString, timeout, timeout, new CusZkSerializer());
        return zk;
    }

    @Bean(name = "zkClient3")
    public ZookeeperClient zookeeperClient() {
        ZookeeperClient zc = new ZookeeperClient(connectString, timeout);
        return zc;
    }

}
