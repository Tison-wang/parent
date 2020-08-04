package com.spring.boot.classloader;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

/**
 * @author
 * @version 1.0
 * @date 2020/7/20 16:10
 */
public class GCTest2 {

    //private final Integer age = 300;

    private static final Integer gender = 600;

    //private static String name;

    //private static String info = "hello";

    /*static {
        name = "Lucy";
    }*/

    public static void main(String[] args) {
        GCTest2 test = new GCTest2();
        test.test();
        System.gc();
        System.out.println("存在引用：" + test);
        System.out.println("=============================");
        test = null;
        System.gc();
        System.out.println("引用置空：test = null");
        System.out.println("内存中的GCTest2对象存活情况：" + test);
    }

    void test() {
        System.out.println("hi");
    }
}

