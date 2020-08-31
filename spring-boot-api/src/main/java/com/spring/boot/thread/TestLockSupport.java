package com.spring.boot.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

/**
 * @author
 * @version 1.0
 * @date 2020/8/10 15:23
 */
public class TestLockSupport {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        // 实现线程B
        final Thread threadB = new Thread(() -> {
            if (list.size() != 5) {
                System.out.println("size 不等于 5，线程B暂时停泊");
                // size 不等于 5，线程B暂时停泊
                LockSupport.park();
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程B收到通知，开始执行自己的业务...");
        });

        // 实现线程A
        Thread threadA = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                list.add("abc");
                System.out.println("线程A向list中添加一个元素，此时list中的元素个数为：" + list.size());
                if (list.size() == 5) {
                    // size 等于 5，解除线程B的停泊
                    LockSupport.unpark(threadB);
                    System.out.println("size 等于 5，解除线程B的停泊");
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        threadA.start();
        threadB.start();
    }

}
