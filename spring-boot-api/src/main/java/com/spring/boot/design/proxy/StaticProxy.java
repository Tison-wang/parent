package com.spring.boot.design.proxy;

/**
 * static proxy
 *
 * @version 1.0
 * @date 2020/7/15 9:50
 */
public class StaticProxy implements IPrint {

    private IPrint print;

    public void setPrint(IPrint print) {
        this.print = print;
    }

    public StaticProxy() {

    }

    public StaticProxy(IPrint print) {
        this.print = print;
    }

    @Override
    public void print() {
        before();
        print.print();
        after();
    }

    private void before() {
        System.out.println("======记录日志前======");
    }

    private void after() {
        System.out.println("======记录日志后======");
    }

    public static void main(String[] args) {
        IPrint printProxy = new StaticProxy(new Print());
        printProxy.print();
        StaticProxy printProxy1 = new StaticProxy();
        printProxy1.setPrint(new Print());
        printProxy1.print();
    }

}
