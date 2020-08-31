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
public class TestReentrantLock1 {

    private static ReentrantLock lock = new ReentrantLock(true);

    private static Integer value = 10;

    // 调用lock方法来获得同步监视器
    private static Condition condition = lock.newCondition();

    public static void main(String[] args) {
        Message message = new Message();
        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {
                message.methodA();
            }).start();
        }

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
                    // singal方法可以唤醒同一个Condition（对象监视器）监视下的线程
                    System.out.println("size=5，我释放了锁，但得等这个同步块的代码完全执行结束，唤醒同一个Condition（对象监视器）监视下的线程");
                    condition.signal();
                }
                if (list.size() == 10) {
                    System.out.println("同步块执行结束，我完全释放了锁，其他线程可以来竞争锁了");
                    condition.signal();
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
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("线程B收到通知，开始执行自己的业务...");
            lock.unlock();
        });
        threadB.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadA.start();
    }


    static class Message {

        public void methodA() {
            // 这个锁我拿来用了，其他线程要用得先等我把内部代码执行完。
            lock.lock();
            try {
                --value;
                System.out.println(Thread.currentThread().getName() + "-" + value);
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // 这个锁我用完，我释放了，其他线程可以来竞争获取了。
                lock.unlock();
            }
        }

        public void methodB() {
            lock.lock();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

    }

}

