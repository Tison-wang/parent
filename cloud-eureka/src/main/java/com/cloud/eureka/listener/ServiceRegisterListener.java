package com.cloud.eureka.listener;

import com.cloud.eureka.utils.ObjectByteConvert;
import com.netflix.appinfo.InstanceInfo;
import com.tszk.common.api.route.ZuulRoute;
import com.tszk.common.api.utils.ZkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRenewedEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaRegistryAvailableEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaServerStartedEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务注册监听器
 *
 * @author
 * @version 1.0
 * @date 2020/4/3 14:53
 */
@Component
@ComponentScan
public class ServiceRegisterListener {

    @Autowired
    private ZkUtils zkUtils;

    private final static Logger logger = LoggerFactory.getLogger(ServiceRegisterListener.class);

    @EventListener(condition = "#event.replication==false")
    public void listen(EurekaInstanceCanceledEvent event) {
        logger.info("服务名：{}, {} 已下线", event.getAppName(), event.getServerId());
    }

    @EventListener(condition = "#event.replication==false")
    public void listen(EurekaInstanceRegisteredEvent event) {
        InstanceInfo instanceInfo = event.getInstanceInfo();
        logger.info("发现服务：{} {} : {} 有状态变化", instanceInfo.getAppName(), instanceInfo.getHostName(), instanceInfo.getPort());
        InstanceInfo.InstanceStatus status = instanceInfo.getStatus();
        List<ZuulRoute> routeList = new ArrayList<>();
        byte[] bytes = zkUtils.getDataForByte("/zk-watcher-1", null);
        if(bytes != null) {
            routeList = ObjectByteConvert.toObject(bytes);
        }
        // 有发现服务上线，向 zookeeper 节点注册服务信息
        if(status.equals(InstanceInfo.InstanceStatus.UP)) {
            logger.info("服务：{} 注册成功", instanceInfo.getAppName());
            // 将zuul本身服务过滤
            if(instanceInfo.getPort() != 8086) {
                ZuulRoute zuulRoute = ZuulRoute.builder()
                        .id(instanceInfo.getAppName().toLowerCase())
                        .port(instanceInfo.getPort())
                        .serviceId(instanceInfo.getAppName().toLowerCase())
                        .path("/" + instanceInfo.getAppName().toLowerCase() + "/**")
                        .url(null)
                        .enabled(true)
                        .retryable(true)
                        .build();
                routeList.add(zuulRoute);
                routeList = routeList.stream().distinct().collect(Collectors.toList());
                zkUtils.updateNode("/zk-watcher-1", ObjectByteConvert.toByteArray(routeList));
            }
        }
        // 有发现服务下线，向 zookeeper 节点删除注册的服务信息
        else if(status.equals(InstanceInfo.InstanceStatus.DOWN)) {
            logger.info("服务：{} 下线", instanceInfo.getAppName());
            routeList = routeList.stream().filter(e -> (e.getPort() != instanceInfo.getPort() && e.getPort() != 0)).distinct().collect(Collectors.toList());
            zkUtils.updateNode("/zk-watcher-1", ObjectByteConvert.toByteArray(routeList));
        }
    }

    @EventListener
    public void listen(EurekaInstanceRenewedEvent event) {
        logger.info("服务 {} 进行续约", event.getServerId() +"  "+ event.getAppName());
    }

    @EventListener
    public void listen(EurekaRegistryAvailableEvent event) {
        logger.info("注册中心可用");
    }

    @EventListener
    public void listen(EurekaServerStartedEvent event) {
        logger.info("注册中心服务端启动完毕");
    }

}
