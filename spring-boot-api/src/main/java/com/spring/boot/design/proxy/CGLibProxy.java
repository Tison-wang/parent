package com.spring.boot.design.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @version 1.0
 * @date 2020/7/15 15:11
 */
public class CGLibProxy implements MethodInterceptor {

    /**
     * CGLib需要代理的目标对象
     */
    private Object targetObject;

    public Object createProxyObject(Object obj) {
        this.targetObject = obj;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(obj.getClass());
        enhancer.setCallback(this);
        Object proxyObj = enhancer.create();
        // 返回代理对象
        return proxyObj;
    }

    @Override
    public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object obj = null;
        before();
        obj = method.invoke(targetObject, args);
        after();
        return obj;
    }

    private void before() {
        System.out.println("======记录日志前======");
    }

    private void after() {
        System.out.println("======记录日志后======");
    }

    public static void main(String[] args) {
        CGLibProxy cgLibProxy = new CGLibProxy();
        // 通过CGLIB动态代理获取代理对象的过程
        /*Enhancer enhancer = new Enhancer();
        // 设置enhancer对象的父类
        enhancer.setSuperclass(Print.class);
        // 设置enhancer的回调对象
        enhancer.setCallback(cgLibProxy);
        // 创建代理对象
        Print print = (Print) enhancer.create();*/

        Print print1 = (Print) cgLibProxy.createProxyObject(new Print());
        //print.print();
        print1.print();
    }

}
