package com.spring.boot.classloader;

/**
 * 重写
 *
 * @author
 * @version 1.0
 * @date 2020/7/22 11:02
 */
public class OverRide {

    public static void main(String[] args) {
        System.out.println("============动态分派============");
        System.out.println("======(依赖运行时的实际类型)======");
        Human man = new Man();
        man.sayHello("Lisy");
        Human woman = new Woman();
        man.sayHello();
        woman.sayHello();
        man = new Woman();
        man.sayHello("Lucy");
    }
}

abstract class Human {
    //protected abstract void sayHello();
    public void sayHello() {
        System.out.println("hi Human!");
    }

    public final void sayHello(String str) {
        System.out.println("hi " + str + " !");
    }
}

class Man extends Human {

    public void sayHello() {
        System.out.println("hi man!");
    }
}

class Woman extends Human {

    public void sayHello() {
        System.out.println("hi woman!");
    }
}