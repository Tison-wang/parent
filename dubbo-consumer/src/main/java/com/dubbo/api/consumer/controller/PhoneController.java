package com.dubbo.api.consumer.controller;

import com.base.common.response.Response;
import com.dubbo.api.service.IPhoneService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author
 * @version 1.0
 * @date 2020/8/4 15:39
 */
@Slf4j
@RestController
public class PhoneController {


    // @Reference 引用dubboProvider暴露的服务
    @Reference(check = false, timeout = 2000, retries = 0, cluster = "failfast")
    private IPhoneService phoneService;

    @ResponseBody
    @HystrixCommand(fallbackMethod = "nativeMethod")
    @RequestMapping(value = "/hello")
    public Response hello() {
        String hello = phoneService.call("151 0595 3322");
        log.info(hello);
        return Response.ok(hello);
    }

    //当远程方法调用失败,会触发当前方法
    public Response nativeMethod() {

        return Response.ok("服务暂时不可用！");
    }
}
