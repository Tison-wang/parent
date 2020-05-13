package com.cloud.zuul.listener;

import com.base.common.utils.ObjectByteConvert;
import com.tszk.common.api.listener.AbstractWatcherApi;
import com.tszk.common.api.route.ZuulRoute;
import com.tszk.common.api.utils.ZkUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2020/4/7 11:01
 */
@Slf4j
@Component
public class ZuulZkWatcher extends AbstractWatcherApi {

    @Autowired
    private ZkUtils zkUtils;

    @Autowired
    private RouteLocator routeLocator;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public void zkListener(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected == event.getState()) {

            // 修改节点
            if (Event.EventType.NodeDataChanged == event.getType()) {
                log.info("zuul zk 路由配置事件监听");
                byte[] data = zkUtils.getDataForByte(event.getPath(), null);
                List<ZuulRoute> routeList = ObjectByteConvert.toObject(data);
                if (CollectionUtils.isEmpty(routeList)) {
                    log.info("路由更新后：无");
                } else {
                    routeList.stream().forEach(e -> {
                        log.info("路由更新后：{}-{}", e.getServiceId(), e.getPort());
                    });
                }
                RoutesRefreshedEvent routesRefreshedEvent = new RoutesRefreshedEvent(routeLocator);
                publisher.publishEvent(routesRefreshedEvent);
            }

        }
    }

    @Override
    public void callBack() {
        log.info("[zuul zk 路由配置事件监听【结束】]");
    }

}
