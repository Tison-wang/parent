package com.tszk.common.api.config;

import com.tszk.common.api.listener.CusZkSerializer;
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

    private static ZooKeeper zooKeeper = null;

    @Bean(name = "zkClient2")
    public ZkClient zkClient2() {

        ZkClient zk = new ZkClient(connectString, timeout, timeout, new CusZkSerializer());

        zk.subscribeDataChanges("/zk-watcher-1", new IZkDataListener(){

            @Override
            public void handleDataChange(String path, Object data)
                    throws Exception {
                String value = (String) data;
                log.info("监听到[{}]配置文件被修改：{}", path, value);
            }

            @Override
            public void handleDataDeleted(String arg0) throws Exception {
                System.out.println("监听到配置文件被删除");
            }

        });

        zk.subscribeDataChanges("/zk-watcher-2", new IZkDataListener(){

            @Override
            public void handleDataChange(String path, Object data)
                    throws Exception {
                String value = (String) data;
                log.info("监听到[{}]配置文件被修改：{}", path, value);
            }

            @Override
            public void handleDataDeleted(String arg0) throws Exception {
                System.out.println("监听到配置文件被删除");
            }

        });

        return zk;
    }

    @Bean(name = "zkClient")
    public ZooKeeper zkClient() throws IOException, InterruptedException {
        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            // 连接成功后，会回调watcher监听，此连接操作是异步的，执行完new语句后，直接调用后续代码
            // 可指定多台服务地址 127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183
            zooKeeper = new ZooKeeper(connectString, timeout, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    log.info("[ZooKeeper链接事件通知]");
                    log.info("当前状态为："+ event.getState());
                    log.info("通知类型为："+ event.getType());
                    log.info("操作的节点路径："+ event.getPath());
                    if(Event.KeeperState.SyncConnected==event.getState()){
                        // 如果收到了服务端的响应事件,连接成功
                        countDownLatch.countDown();
                        if (Event.EventType.None == event.getType()){
                            log.info("--------连接事件回调--------");
                        }
                        //创建节点
                        if(Event.EventType.NodeCreated == event.getType()){
                            log.info("------创建节点事件回调------");
                        }
                        //修改节点
                        if(Event.EventType.NodeDataChanged == event.getType()){
                            log.info("------修改节点事件回调------");
                        }
                        //删除节点
                        if(Event.EventType.NodeDeleted == event.getType()){
                            log.info("------删除节点事件回调------");
                        }
                    }
                    if(Event.KeeperState.Disconnected==event.getState()) {
                        log.info("与zk断开......");
                    }
                }
            });
            countDownLatch.await();
            log.info("[初始化ZooKeeper连接状态]={}", zooKeeper.getState());
        } catch (Exception e) {
            log.error("[初始化ZooKeeper连接异常]={}", e);
        }

        return  zooKeeper;
    }

}
