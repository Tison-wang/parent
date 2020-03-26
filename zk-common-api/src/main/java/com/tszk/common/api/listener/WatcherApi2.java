package com.tszk.common.api.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * Watcher监听
 *
 * @author
 * @version 1.0
 * @date 2020/3/9 17:35
 */
@Slf4j
public class WatcherApi2 implements Watcher {

    @Override
    public void process(WatchedEvent event) {
        // 三种监听类型： 创建，删除，更新
        log.info("【zk-common-api2监听状态】={}",event.getState());
        log.info("【zk-common-api2监听路径为】={}",event.getPath());
        log.info("【zk-common-api2监听的类型为】={}",event.getType());
        if(Event.KeeperState.SyncConnected==event.getState()){
            if (Event.EventType.None == event.getType()){
                log.info("--------[zk-common-api2]自定义连接事件回调--------");
            }
            //创建节点
            if(Event.EventType.NodeCreated == event.getType()){
                log.info("------[zk-common-api2]自定义创建节点事件回调------");
            }
            //修改节点
            if(Event.EventType.NodeDataChanged == event.getType()){
                log.info("------[zk-common-api2]自定义修改节点事件回调------11111");
            }
            //删除节点
            if(Event.EventType.NodeDeleted == event.getType()){
                log.info("------[zk-common-api2]自定义删除节点事件回调------");
            }
        }
    }

}
