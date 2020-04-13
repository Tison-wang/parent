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

    private String path;

    private AbstractWatcherApi chainedWatcher;

    public boolean dead;

    private DataMonitorListener listener;

    private byte prevData[];

    public DataMonitor(ZooKeeper zk, String path, AbstractWatcherApi chainedWatcher, DataMonitorListener listener) {
        this.zk = zk;
        this.path = path;
        this.chainedWatcher = chainedWatcher;
        this.listener = listener;
        // Get things started by checking if the node exists. We are going
        // to be completely event driven
        zk.exists(path, true, this, null);
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
        log.info("[Zookeeper]节点变化事件通知, 当前状态为：" + event.getState() + ",\t通知类型为：" + event.getType() + ",\t操作的节点路径：" + path);
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
        } else {
            if (path != null && path.equals(path)) {
                // Something has changed on the node, let's find out
                zk.exists(path, true, this, null);
            }
        }

        /*
         * 执行自定义监听事件 process
         * date: 2020-04-13 17:50:00
         */
        if (chainedWatcher != null) {
            chainedWatcher.process(event);
        }
    }

    public void processResult(int rc, String path, Object ctx, Stat stat) {
        boolean exists;
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
                zk.exists(path, true, this, null);
                return;
        }

        byte b[] = null;
        if (exists) {
            try {
                b = zk.getData(path, false, null);
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

        /*
         * 监听事件 process 执行结束后回调
         * date: 2020-04-13 17:50:00
         */
        if (chainedWatcher != null) {
            chainedWatcher.callBack();
        }
    }

}
