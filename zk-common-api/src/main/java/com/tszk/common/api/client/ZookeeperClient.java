package com.tszk.common.api.client;

import com.tszk.common.api.listener.AbstractWatcherApi;
import com.tszk.common.api.listener.Executor;
import com.tszk.common.api.utils.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 *
 *
 * @author
 * @version 1.0
 * @date 2020/4/14 9:05
 */
@Slf4j
public class ZookeeperClient implements Watcher {

    private ZooKeeper zooKeeper = null;

    private int timeout;

    private String connectString;

    private CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public ZookeeperClient(String connectString, int timeout) {
        try {
            this.timeout = timeout;
            this.connectString = connectString;
            this.zooKeeper = new ZooKeeper(connectString, timeout, this);
            connectedSemaphore.await();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 订阅监听路径
     *
     * @author
     * @date 2020/3/25 15:05
     * @param path 监听路径，示例：/config
     * @param watcher 自定义监听器
     */
    public void subDataChange(String path, AbstractWatcherApi watcher) {
        ThreadPoolUtil.getInstance().executeTask(new Runnable() {
            @Override
            public void run() {
                try {
                    new Executor(connectString, path, watcher).run();
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void process(WatchedEvent event) {
        try {
            log.info("ZooKeeper 连接事件通知, 当前状态为：{}", event.getState());
            if(Event.KeeperState.SyncConnected == event.getState()){
                // 如果收到了服务端的响应事件,连接成功
                connectedSemaphore.countDown();
            } else if(Event.KeeperState.Disconnected == event.getState()) {
                log.info("与zk断开......");
            } else if (Event.KeeperState.Expired == event.getState()) {
                try {
                    zooKeeper = new ZooKeeper(connectString, timeout, this);
                    connectedSemaphore.await();
                } catch (IOException e) {
                    log.warn("fail to connect to zoo keeper", e);
                }
            }
            log.info("ZooKeeper 连接成功，状态：{}", zooKeeper.getState());
        } catch (Exception e) {
            log.error("[初始化ZooKeeper连接异常]={}", e);
        }
    }

}
