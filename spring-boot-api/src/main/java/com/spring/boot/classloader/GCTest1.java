package com.spring.boot.classloader;

/**
 * 垃圾回收测试
 *
 * @version 1.0
 * @date 2020/7/16 17:59
 */
public class GCTest1 {
    public static GCTest1 test;

    public void isAlive() {
        System.out.println("I am alive :)");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalize method executed!");
        test = this;
    }

    public static void main(String[] args) throws Exception {
        test = new GCTest1();
        test = null;
        System.gc();
        Thread.sleep(500);
        if (test != null) {
            test.isAlive();
        } else {
            System.out.println("no,I am dead :(");
        }
        // 下面代码与上面完全一致，但是此次自救失败
        test = null;
        System.gc();
        Thread.sleep(500);
        if (test != null) {
            test.isAlive();
        } else {
            System.out.println("no,I am dead :(");
        }
    }
}
