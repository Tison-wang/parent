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
public abstract class AbstractWatcherApi implements Watcher {

    @Override
    public void process(WatchedEvent event) {
        // 三种监听类型： 创建，删除，更新
        this.zkListener(event);
    }

    /**
     * 自定义节点监听回调方法
     *
     * @author
     * @date 2020/4/8 9:28
     * @param event 监听事件
     * @return
     */
    public abstract void zkListener(WatchedEvent event);

    /**
     * 监听事件执行结束后调用
     *
     * @author
     * @date 2020/4/8 9:28
     */
    public abstract void callBack();

}
