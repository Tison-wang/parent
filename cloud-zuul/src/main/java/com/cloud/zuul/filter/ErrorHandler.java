package com.cloud.zuul.filter;

import com.alibaba.fastjson.JSON;
import com.base.common.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author
 * @version 1.0
 * @date 2020/5/14 10:22
 */
@Slf4j
@RestController
public class ErrorHandler implements ErrorController {

    @Override
    public String getErrorPath() {
        return "error";
    }

    @RequestMapping(value = "/error")
    public Response error(HttpServletRequest req, HttpServletResponse res) {
        String exceptionMsg = req.getAttribute("exception").toString();
        log.error("执行错误处理 /error，异常原因：{}", exceptionMsg);
        log.error(req.getAttribute("javax.servlet.error.status_code").toString());
        log.error(req.getAttribute("javax.servlet.error.exception").toString());
        if (req.getAttribute("javax.servlet.error.status_code").equals(429)) {
            return Response.failure("接口访问太频繁，请稍后重试!", 429);
        } else {
            return JSON.parseObject(exceptionMsg, Response.class);
        }
    }

}
