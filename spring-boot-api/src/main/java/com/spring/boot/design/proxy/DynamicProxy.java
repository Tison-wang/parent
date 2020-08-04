package com.spring.boot.design.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * dynamic proxy
 *
 * @version 1.0
 * @date 2020/7/15 10:12
 */
public class DynamicProxy implements InvocationHandler {

    private Object target;

    public DynamicProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        Object result = method.invoke(target, args);
        after();
        return result;
    }

    private void before() {
        System.out.println("======记录日志前======");
    }

    private void after() {
        System.out.println("======记录日志后======");
    }

    public <T> T getProxy() {
        ClassLoader loader = target.getClass().getClassLoader();
        Class[] interfaces = target.getClass().getInterfaces();
        return (T) Proxy.newProxyInstance(loader, interfaces, this);
    }

    public static void main(String[] args) {
        IPrint print = new Print();
        DynamicProxy dynamicProxy = new DynamicProxy(print);
        /*
         * 动态代理原生用法
         */
        /*ClassLoader loader = Print.class.getClassLoader();
        Class[] interfaces = Print.class.getInterfaces();
        IPrint printProxy = (IPrint) Proxy.newProxyInstance(loader, interfaces, dynamicProxy);*/
        IPrint printProxy = dynamicProxy.getProxy();
        printProxy.print();
    }

}
