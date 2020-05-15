package com.spring.boot.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * jersey 全局异常处理类
 *
 * @author Tison
 * @version 1.0
 * @date 2019/12/25 13:58
 */
@Provider
public class JerseyExceptionMapper implements ExceptionMapper<Exception> {

    private final static Logger logger = LoggerFactory.getLogger(JerseyExceptionMapper.class);

    @Override
    public Response toResponse(Exception ex) {
        logger.info("【exception happened】: ", ex);
        return Response
                .status(Response.Status.BAD_REQUEST.getStatusCode())
                .type(MediaType.APPLICATION_JSON)
                .entity(com.base.common.response.Response.failure(ex.getMessage(), 501, "系统异常"))
                .build();
    }
}
