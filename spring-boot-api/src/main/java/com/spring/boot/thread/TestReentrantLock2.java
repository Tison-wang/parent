package com.spring.boot.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author
 * @version 1.0
 * @date 2020/8/10 11:27
 */
public class TestReentrantLock2 {

    private static ReentrantLock lock = new ReentrantLock(true);

    // 调用lock方法来获得同步监视器
    private static Condition condition1 = lock.newCondition();
    private static Condition condition2 = lock.newCondition();

    public static void main(String[] args) {

        List<String> list = new ArrayList<>();
        // 实现线程A
        Thread threadA = new Thread(() -> {
            lock.lock();
            for (int i = 1; i <= 10; i++) {
                list.add("abc");
                System.out.println("线程A向list中添加一个元素，此时list中的元素个数为：" + list.size());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (list.size() == 5) {
                    // singal方法可以唤醒同一个Condition1（对象监视器）监视下的线程
                    System.out.println("size=5，我释放了锁，但得等这个同步块的代码完全执行结束，才唤醒同一个Condition1（对象监视器）监视下的线程");
                    condition1.signal();
                }
                if (list.size() == 8) {
                    // singal方法可以唤醒同一个Condition2（对象监视器）监视下的线程
                    System.out.println("size=8，我释放了锁，但得等这个同步块的代码完全执行结束，才唤醒同一个Condition2（对象监视器）监视下的线程");
                    condition2.signal();
                }
                if (list.size() == 10) {
                    System.out.println("同步块执行结束，我完全释放了锁，其他线程可以来竞争锁了");
                }

            }
            lock.unlock();
        });

        // 实现线程B
        Thread threadB = new Thread(() -> {
            lock.lock();
            if (list.size() != 5) {
                try {
                    System.out.println("线程B在等待通知......");
                    condition1.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("线程B收到通知，开始执行自己的业务...");
            lock.unlock();
        });
        threadB.start();

        // 实现线程C
        Thread threadC = new Thread(() -> {
            lock.lock();
            if (list.size() != 8) {
                try {
                    System.out.println("线程C在等待通知......");
                    condition2.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("线程C收到通知，开始执行自己的业务...");
            lock.unlock();
        });
        threadC.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadA.start();
    }

}

