package com.tszk.common.api.utils;

import com.base.common.utils.ThreadPoolUtil;
import com.tszk.common.api.client.ZookeeperClient;
import com.tszk.common.api.listener.AbstractWatcherApi;
import com.tszk.common.api.listener.Executor;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * zk客户端封装工具类
 *
 * @author
 * @version 1.0
 * @date 2020/3/9 17:33
 */
@Slf4j
@Component
public class ZkUtils {

    @Value("${zookeeper.address}")
    private String connectString;

    @Value("${zookeeper.timeout}")
    private int timeout;

    @Autowired
    private ZookeeperClient zkClient3;

    /**
     * 订阅监听路径
     *
     * @param path    监听路径，示例：/config
     * @param watcher 自定义监听器
     * @author
     * @date 2020/3/25 15:05
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

    /**
     * 判断指定节点是否存在
     *
     * @param path
     * @param needWatch 指定是否复用zookeeper中默认的Watcher
     * @return
     */
    public Stat exists(String path, boolean needWatch) {
        try {
            return zkClient3.getZooKeeper().exists(path, needWatch);
        } catch (Exception e) {
            log.error("【断指定节点是否存在异常】{},{}", path, e);
            return null;
        }
    }

    /**
     * 检测结点是否存在 并设置监听事件
     * 三种监听类型： 创建，删除，更新
     *
     * @param path
     * @param watcher 传入指定的监听类
     * @return
     */
    public Stat exists(String path, Watcher watcher) {
        try {
            return zkClient3.getZooKeeper().exists(path, watcher);
        } catch (Exception e) {
            log.error("【断指定节点是否存在异常】{},{}", path, e);
            return null;
        }
    }

    /**
     * 创建持久化节点
     *
     * @param path
     * @param data
     */
    public boolean createNode(String path, String data) {
        try {
            zkClient3.getZooKeeper().create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            return true;
        } catch (Exception e) {
            log.error("【创建持久化节点异常】{},{},{}", path, data, e);
            return false;
        }
    }


    /**
     * 修改持久化节点
     *
     * @param path
     * @param data
     */
    public boolean updateNode(String path, String data) {
        try {
            //zk的数据版本是从0开始计数的。如果客户端传入的是-1，则表示zk服务器需要基于最新的数据进行更新。如果对zk的数据节点的更新操作没有原子性要求则可以使用-1.
            //version参数指定要更新的数据的版本, 如果version和真实的版本不同, 更新操作将失败. 指定version为-1则忽略版本检查
            zkClient3.getZooKeeper().setData(path, data.getBytes(), -1);
            return true;
        } catch (Exception e) {
            log.error("【修改持久化节点异常】{},{},{}", path, data, e);
            return false;
        }
    }

    /**
     * 修改持久化节点
     *
     * @param path
     * @param data
     */
    public boolean updateNode(String path, byte[] data) {
        try {
            //zk的数据版本是从0开始计数的。如果客户端传入的是-1，则表示zk服务器需要基于最新的数据进行更新。如果对zk的数据节点的更新操作没有原子性要求则可以使用-1.
            //version参数指定要更新的数据的版本, 如果version和真实的版本不同, 更新操作将失败. 指定version为-1则忽略版本检查
            zkClient3.getZooKeeper().setData(path, data, -1);
            return true;
        } catch (Exception e) {
            log.error("【修改持久化节点异常】{},{},{}", path, data, e);
            return false;
        }
    }

    /**
     * 删除持久化节点
     *
     * @param path
     */
    public boolean deleteNode(String path) {
        try {
            //version参数指定要更新的数据的版本, 如果version和真实的版本不同, 更新操作将失败. 指定version为-1则忽略版本检查
            zkClient3.getZooKeeper().delete(path, -1);
            return true;
        } catch (Exception e) {
            log.error("【删除持久化节点异常】{},{}", path, e);
            return false;
        }
    }

    /**
     * 获取当前节点的子节点(不包含孙子节点)
     *
     * @param path 父节点path
     */
    public List<String> getChildren(String path) throws KeeperException, InterruptedException {
        List<String> list = zkClient3.getZooKeeper().getChildren(path, false);
        return list;
    }

    /**
     * 获取指定节点的值
     *
     * @param path
     * @return
     */
    public String getDataForString(String path, Watcher watcher) {
        try {
            Stat stat = new Stat();
            byte[] bytes = zkClient3.getZooKeeper().getData(path, watcher, stat);
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取指定节点的值
     *
     * @param path
     * @return
     */
    public byte[] getDataForByte(String path, Watcher watcher) {
        try {
            Stat stat = new Stat();
            return zkClient3.getZooKeeper().getData(path, watcher, stat);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
