package com.tszk.common.api.listener;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Description:
 *
 * @author
 * @version 1.0
 * @date 2020/3/20 16:02
 */
public class Executor implements Watcher, Runnable, DataMonitor.DataMonitorListener {

    private DataMonitor dm;

    private ZooKeeper zk;

    private Process child;

    public Executor(String hostPort, String znode, AbstractWatcherApi watcher) throws KeeperException, IOException {
        zk = new ZooKeeper(hostPort, 3000, this);
        dm = new DataMonitor(zk, znode, watcher, this);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        args = new String[]{"127.0.0.1:2181","/zk-watcher-1"};
        String hostPort = args[0];
        String znode = args[1];
        try {
            new Executor(hostPort, znode,null).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***************************************************************************
     * We do process any events ourselves, we just need to forward them on.
     *
     * @see Watcher# process(org.apache.zookeeper.proto.WatcherEvent)
     */
    public void process(WatchedEvent event) {
        if(this.dm != null) {
            dm.process(event);
        }
    }

    public void run() {
        try {
            synchronized (this) {
                while (!dm.dead) {
                    wait();
                }
            }
        } catch (InterruptedException e) {
        }
    }

    public void closing(int rc) {
        synchronized (this) {
            notifyAll();
        }
    }

    static class StreamWriter extends Thread {
        OutputStream os;

        InputStream is;

        StreamWriter(InputStream is, OutputStream os) {
            this.is = is;
            this.os = os;
            start();
        }

        public void run() {
            byte b[] = new byte[80];
            int rc;
            try {
                while ((rc = is.read(b)) > 0) {
                    os.write(b, 0, rc);
                }
            } catch (IOException e) {
            }

        }
    }

    public void exists(byte[] data) {
        if (data == null) {
            if (child != null) {
                System.out.println("Killing process");
                child.destroy();
                try {
                    child.waitFor();
                } catch (InterruptedException e) {
                }
            }
            child = null;
        } else {
            if (child != null) {
                System.out.println("Stopping child");
                child.destroy();
                try {
                    child.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //other class
        }
    }
}
