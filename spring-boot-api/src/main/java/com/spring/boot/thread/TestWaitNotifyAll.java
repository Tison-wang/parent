package com.spring.boot.thread;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2020/8/7 16:33
 */
public class TestWaitNotifyAll {
    // wait和 notify必须配合synchronized使用，wait方法释放锁，notify方法不释放锁

    public static void main(String[] args) {

        // 定义一个锁对象
        Object lock = new Object();
        List<String> list = new ArrayList<>();
        // 实现线程A
        Thread threadA = new Thread(() -> {
            // 获取对象锁
            synchronized (lock) {
                for (int i = 1; i <= 10; i++) {
                    list.add("abc");
                    System.out.println("线程A向list中添加一个元素，此时list中的元素个数为：" + list.size());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (list.size() == 5) {
                        // 我唤醒BCD线程，但是我要等我这个同步块的业务执行完，才释放锁
                        System.out.println("我是线程A，size==5，notify唤醒BCD线程，但是我要等我这个同步块的业务执行完，才释放锁");
                        lock.notifyAll();
                    }
                    if (list.size() == 10) {
                        System.out.println("我是线程A，我执行完了，释放锁了。");
                    }
                }

            }
        });

        // 实现线程B
        Thread threadB = new Thread(() -> {
            while (true) {
                // 获取对象锁
                synchronized (lock) {
                    if (list.size() != 5) {
                        System.out.println("我是线程B。listSize!=5，我要等线程A的size变为5，现在wait等待吧（释放锁，让线程A获取锁去增加size的值）");
                        try {
                            // 释放对象锁
                            lock.wait();
                            System.out.println("等待结束，线程B收到通知，线程B可以执行自己的后续业务了...");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("线程B收到通知，开始执行自己的业务...");
                    if (list.size() == 10) {
                        list.remove(0);
                        break;
                    }
                }
            }
        });
        // 实现线程C
        Thread threadC = new Thread(() -> {
            while (true) {
                // 获取对象锁
                synchronized (lock) {
                    if (list.size() != 5) {
                        System.out.println("我是线程C。listSize!=5，我要等线程A的size变为5，现在wait等待吧（释放锁，让线程A获取锁去增加size的值）");
                        try {
                            // 释放对象锁
                            lock.wait();
                            System.out.println("等待结束，线程C收到通知，线程C可以执行自己的后续业务了...");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("线程C收到通知，开始执行自己的业务...");
                    if (list.size() == 10) {
                        list.remove(0);
                        break;
                    }
                }
            }
        });
        // 实现线程D
        Thread threadD = new Thread(() -> {
            while (true) {
                // 获取对象锁
                synchronized (lock) {
                    if (list.size() != 5) {
                        System.out.println("我是线程D。listSize!=5，我要等线程A的size变为5，现在wait等待吧（释放锁，让线程A获取锁去增加size的值）");
                        try {
                            // 释放对象锁
                            lock.wait();
                            System.out.println("等待结束，线程D收到通知，线程D可以执行自己的后续业务了...");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("线程D收到通知，开始执行自己的业务...");
                    if (list.size() == 10) {
                        list.remove(0);
                        break;
                    }
                }
            }
        });

        //　需要先启动线程B,C,D
        threadB.start();
        threadC.start();
        threadD.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 再启动线程A
        threadA.start();
    }

}
