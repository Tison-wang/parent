package com.spring.boot.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author
 * @version 1.0
 * @date 2020/8/12 9:24
 */
public class TestThread {

    private static Integer i = 0;
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();

    public static void main(String[] args) {

        System.out.println("我是主线程，我正在执行...");

        Thread threadA = new Thread(() -> {
            lock.lock();
            i++;
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("我是子线程A，我执行完了，唤醒主线程-i=" + i);
            if (i == 3) {
                condition.signal();
            }
            lock.unlock();
        });

        Thread threadB = new Thread(() -> {
            lock.lock();
            i++;
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("我是子线程B，我执行完了，唤醒主线程-i=" + i);
            if (i == 3) {
                condition.signal();
            }
            lock.unlock();
        });

        Thread threadC = new Thread(() -> {
            lock.lock();
            i++;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("我是子线程C，我执行完了，唤醒主线程-i=" + i);
            if (i == 3) {
                condition.signal();
            }
            lock.unlock();
        });

        lock.lock();

        if (i != 3) {
            System.out.println("我的子线程还未执行完，先暂停后续业务操作...");
            try {
                threadB.start();
                threadC.start();
                threadA.start();
                // await 需在执行子线程唤醒之前先进行调用操作
                // 否则子线程先执行完唤醒操作，主线程才进行await，后面就没有子线程可以来操作唤醒功能了
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("我的子线程全部执行完毕，我要开始操作我的业务了...");

        lock.unlock();

    }

}
