package com.spring.boot.thread;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2020/8/7 16:21
 */
public class TestVolatile {
    // 定义一个共享变量来实现通信，它需要是volatile修饰，否则线程不能及时感知
    static volatile boolean notice = false;

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        // 实现线程A
        Thread threadA = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                list.add("abc" + i);
                System.out.println("线程A向list中添加一个元素，此时list中的元素个数为：" + list.size());
                if (list.size() == 5) {
                    notice = true;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // 实现线程B
        Thread threadB = new Thread(() -> {
            while (true) {
                // notice为true时，线程B监听到后会执行线程B业务逻辑
                if (notice) {
                    System.out.println("线程B收到通知，开始执行自己的业务...");
                    break;
                }
            }
        });

        //　需要先启动线程B
        threadB.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 再启动线程A
        threadA.start();
    }
}
