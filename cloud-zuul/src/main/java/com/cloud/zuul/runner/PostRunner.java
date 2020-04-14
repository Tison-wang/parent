package com.cloud.zuul.runner;

import com.cloud.zuul.listener.ZuulZkWatcher;
import com.tszk.common.api.client.ZookeeperClient;
import com.tszk.common.api.utils.ZkUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *
 *
 * @author
 * @version 1.0
 * @date 2020/4/7 13:52
 */
@Slf4j
@Component
@Order(value = 1)
public class PostRunner implements ApplicationRunner {

    @Autowired
    private ZkUtils zkUtils;

    @Autowired
    private ZookeeperClient zkClient3;

    @Autowired
    private ZuulZkWatcher watcher;

    /**
     * 设置监听 '/zk-watcher-' 节点
     */
    @Override
    public void run(ApplicationArguments var1) throws Exception{
        String path = "/zk-watcher-1";
        Stat stat = zkUtils.exists(path, false);
        if(stat == null) {
            log.info("存储路由配置信息的节点/zk-watcher-1不存在，开始进行创建");
            boolean res = zkUtils.createNode(path, null);
            if(res) {
                log.info("开始监听/zk-watcher-1节点（存储路由配置信息）");
                zkClient3.subDataChange(path, watcher);
            } else {
                log.info("存储路由配置信息的节点/zk-watcher-1创建失败");
            }
        } else {
            log.info("开始监听/zk-watcher-1节点（存储路由配置信息）");
            zkClient3.subDataChange(path, watcher);
        }
    }

}
