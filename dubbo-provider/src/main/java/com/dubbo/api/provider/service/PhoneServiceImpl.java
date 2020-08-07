package com.dubbo.api.provider.service;

import com.dubbo.api.service.IPhoneService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

/**
 * @author
 * @version 1.0
 * @date 2020/8/4 14:25
 */
// @Service是dubbo里的注解，作用是暴露服务
@Slf4j
@Service
@Component
public class PhoneServiceImpl implements IPhoneService {

    @Override
    @HystrixCommand
    public String call(String phoneNum) {
        /*try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        log.info("呼叫手机号为 " + phoneNum + " 的机主...");
        return "呼叫成功!";
    }
}
