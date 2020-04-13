package com.spring.boot.zklistener;

import com.tszk.common.api.listener.AbstractWatcherApi;
import com.tszk.common.api.route.ZuulRoute;
import com.tszk.common.api.utils.ObjectByteConvert;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 *
 * @author
 * @version 1.0
 * @date 2020/4/8 9:35
 */
@Slf4j
@Component
public class ApiZkWatcher extends AbstractWatcherApi {

    @Autowired
    private ZooKeeper zkClient;

    @Override
    public void zkListener(WatchedEvent event) {
        if(Event.KeeperState.SyncConnected==event.getState()){

            // 修改节点
            if(Event.EventType.NodeDataChanged == event.getType()){
                log.info("spring-boot-api路由配置事件监听");
                try {
                    byte[] data = zkClient.getData(event.getPath(), false, new Stat());
                    List<ZuulRoute> routeList = ObjectByteConvert.toObject(data);
                    routeList.stream().forEach(e -> {
                        log.info("路由更新后：{}-{}", e.getServiceId(), e.getPort());
                    });
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void callBack() {
        log.info("[spring-boot-api路由配置事件监听【结束】]");
    }

}
