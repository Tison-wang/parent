package com.spring.boot.jersey;

import com.base.common.response.Response;
import com.google.common.collect.Sets;
import com.spring.boot.classloader.MyClassLoader1;
import com.spring.boot.classloader.MyClassLoader2;
import com.spring.boot.design.proxy.Print;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.InvocationTargetException;

/**
 * @version 1.0
 * @date 2020/7/16 10:01
 */
@Slf4j
@Component
@Path("classloader")
public class JerseyLoadClassResource {

    private Object o;

    private MyClassLoader1 myClassLoader;

    @GET
    @Path("loadClass1")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response loadClass1() {
        log.info("类加载1");
        String swapPath = "D://data//";
        String className = "com.spring.boot.design.proxy.Print";

        //每次都实例化一个ClassLoader，这里传入swap路径，和需要特殊加载的类名
        //MyClassLoader1 myClassLoader = new MyClassLoader1(swapPath, Sets.newHashSet(className));
        myClassLoader = new MyClassLoader1(swapPath, Sets.newHashSet(className));
        try {
            //使用自定义的ClassLoader加载类至内存
            //Object o = myClassLoader.loadClass(className).newInstance();
            o = myClassLoader.loadClass(className).newInstance();
            //并调用该加载的类的print方法验证
            o.getClass().getMethod("print").invoke(o);
        } catch (InstantiationException |
                IllegalAccessException |
                ClassNotFoundException |
                NoSuchMethodException |
                InvocationTargetException ignored) {
        }
        return Response.ok(true);
    }

    @GET
    @Path("loadClass2")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response loadClass2() {
        log.info("类加载2");
        String swapPath = "D://docs//";
        String className = "com.spring.boot.design.proxy.Print";

        //每次都实例化一个ClassLoader，这里传入swap路径，和需要特殊加载的类名
        myClassLoader = new MyClassLoader1(swapPath, Sets.newHashSet(className));
        try {
            //使用自定义的ClassLoader加载类至内存
            o = myClassLoader.loadClass(className).newInstance();
            //并调用该加载的类的print方法验证
            o.getClass().getMethod("print").invoke(o);
        } catch (InstantiationException |
                IllegalAccessException |
                ClassNotFoundException |
                NoSuchMethodException |
                InvocationTargetException ignored) {
        }
        return Response.ok(true);
    }

    @GET
    @Path("loadClass3")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response loadClass3() {
        log.info("类加载3");
        String swapPath = "D://usr//";
        String className = "com.spring.boot.design.proxy.Print";

        try {
            //使用自定义的ClassLoader加载类至内存
            Object o = this.getClass().getClassLoader().loadClass(className).newInstance();
            //并调用该加载的类的print方法验证
            o.getClass().getMethod("print").invoke(o);
        } catch (InstantiationException |
                IllegalAccessException |
                ClassNotFoundException |
                NoSuchMethodException |
                InvocationTargetException ignored) {
        }
        return Response.ok(true);
    }

    @GET
    @Path("print")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response print() {
        log.info("print");
        if (o != null) {
            try {
                o.getClass().getMethod("print").invoke(o);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            log.info("o 对象为null");
        }
        return Response.ok(true);
    }

}
