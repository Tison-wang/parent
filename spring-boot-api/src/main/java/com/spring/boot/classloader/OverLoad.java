package com.spring.boot.classloader;

/**
 * 重载
 *
 * @author
 * @version 1.0
 * @date 2020/7/22 11:13
 */
public class
OverLoad {
    public static void main(String[] args) {
        System.out.println("============静态分派============");
        System.out.println("=========(依赖静态类型)==========");
        人类 man = new 男人();
        人类 woman = new 女人();
        sayHello(man);
        sayHello(woman);
        男人 man1 = new 男人();
        sayHello(man1);
    }

    public static void sayHello(人类 human) {
        System.out.println("hi guy!");
    }

    public static void sayHello(男人 man) {
        System.out.println("hi man!");
    }

    public static void sayHello(女人 woman) {
        System.out.println("hi woman!");
    }

}

abstract class 人类 {

}

class 男人 extends 人类 {

}

class 女人 extends 人类 {

}

