package com.spring.boot.design.proxy;

/**
 * @version 1.0
 * @date 2020/7/15 9:43
 */
public class Print implements IPrint {

    public static String NAME = "Lucy";

    static {
        NAME = "Lucks";
        System.out.println("执行Print.class内的static块");
    }

    @Override
    public void print() {
        System.out.println(this.getClass().getClassLoader());
        System.out.println("我是" + NAME + "，我在打印.哦哦哦");
    }

}
