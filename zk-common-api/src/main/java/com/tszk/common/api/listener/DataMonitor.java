package com.tszk.common.api.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.Arrays;

/**
 * Description:
 *
 * @author
 * @version 1.0
 * @date 2020/3/20 15:58
 */
@Slf4j
public class DataMonitor implements Watcher, AsyncCallback.StatCallback {

    private ZooKeeper zk;

    // 监听节点路径
    private String znode;

    private Watcher chainedWatcher;

    public boolean dead;

    private DataMonitorListener listener;

    private byte prevData[];


    public DataMonitor(ZooKeeper zk, String znode, Watcher chainedWatcher, DataMonitorListener listener) {
        this.zk = zk;
        this.znode = znode;
        this.chainedWatcher = chainedWatcher;
        this.listener = listener;
        // Get things started by checking if the node exists. We are going
        // to be completely event driven
        zk.exists(znode, true, this, null);
    }

    /**
     * Other classes use the DataMonitor by implementing this method
     */
    public interface DataMonitorListener {

        /**
         * The existence status of the node has changed.
         */
        void exists(byte data[]);

        /**
         * The ZooKeeper session is no longer valid.
         *
         * @param rc
         *            the ZooKeeper reason code
         */
        void closing(int rc);
    }

    @Override
    public void process(WatchedEvent event) {
        String path = event.getPath();
        if (event.getType() == Event.EventType.None) {
            // We are are being told that the state of the
            // connection has changed
            switch (event.getState()) {
                case SyncConnected:
                    // In this particular example we don't need to do anything
                    // here - watches are automatically re-registered with
                    // server and any watches triggered while the client was
                    // disconnected will be delivered (in order of course)
                    break;
                case Expired:
                    // It's all over
                    dead = true;
                    listener.closing(Code.SessionExpired);
                    break;
            }
        }
        //创建节点
        else if(Event.EventType.NodeCreated == event.getType()) {
            log.info("------自定义创建节点事件回调------");
            if (chainedWatcher != null) {
                chainedWatcher.process(event);
            }
            try {
                zk.getData(path, this, new Stat());
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //修改节点
        else if(Event.EventType.NodeDataChanged == event.getType()) {
            log.info("来自[DataMonitor]事件通知, 当前状态为："+ event.getState() +",\t通知类型为："+ event.getType() +",\t操作的节点路径："+ event.getPath());
            try {
                String obj = new String(zk.getData(path, this, new Stat()));
                log.info("【节点更新后值为】：value={}", obj);
                if (chainedWatcher != null) {
                    chainedWatcher.process(event);
                }
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //删除节点
        else if(Event.EventType.NodeDeleted == event.getType()) {
            log.info("------自定义删除节点事件回调------");
            if (chainedWatcher != null) {
                chainedWatcher.process(event);
            }
        } else {
            if (path != null && path.equals(znode)) {
                // Something has changed on the node, let's find out
                zk.exists(znode, true, this, null);
            }
        }
    }

    public void processResult(int rc, String path, Object ctx, Stat stat) {
        boolean exists;
        log.info("[DataMonitor]execute DataMonitor.processResult method, monitor path: {}", path);
        switch (rc) {
            case Code.Ok:
                exists = true;
                break;
            case Code.NoNode:
                exists = false;
                break;
            case Code.SessionExpired:
            case Code.NoAuth:
                dead = true;
                listener.closing(rc);
                return;
            default:
                // Retry errors
                zk.exists(znode, true, this, null);
                return;
        }

        byte b[] = null;
        if (exists) {
            try {
                b = zk.getData(znode, false, null);
            } catch (KeeperException e) {
                // We don't need to worry about recovering now. The watch
                // callbacks will kick off any exception handling
                e.printStackTrace();
            } catch (InterruptedException e) {
                return;
            }
        }
        if ((b == null && b != prevData) || (b != null && !Arrays.equals(prevData, b))) {
            listener.exists(b);
            prevData = b;
        }
    }

}
