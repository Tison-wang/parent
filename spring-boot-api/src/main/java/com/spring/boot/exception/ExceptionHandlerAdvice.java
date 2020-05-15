package com.spring.boot.exception;

import com.base.common.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * spring 全局异常处理类
 *
 * @author Tison
 * @version 1.0
 * @date 2019/12/25 10:20
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response handleUnexpectedServer(Exception ex) {
        logger.info("【exception happened】: ", ex);
        return Response.failure(ex.getMessage(), 500, "系统异常");
    }

    // 系统异常处理
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public Response exception(Throwable throwable) {
        logger.info("【exception happened】: ", throwable);
        return Response.failure(throwable.getMessage(), 500, "系统异常");
    }

}
