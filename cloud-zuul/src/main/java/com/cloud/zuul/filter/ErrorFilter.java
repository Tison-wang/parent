package com.cloud.zuul.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.common.response.Response;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.MediaType;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * @version 1.0
 * @date 2020/4/15 11:13
 */
@Slf4j
public class ErrorFilter extends ZuulFilter {

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        try {
            RequestContext ctx = RequestContext.getCurrentContext();
            Throwable throwable = ctx.getThrowable();
            log.error("error异常信息:", throwable);
            HttpServletRequest request = ctx.getRequest();
            HttpServletResponse response = ctx.getResponse();
            response.setStatus(500);
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            if (Objects.nonNull(ctx.getResponseBody())) {
                Response result = JSON.parseObject(ctx.getResponseBody(), Response.class);
                request.setAttribute("exception", result);
                response.getOutputStream().write(ctx.getResponseBody().getBytes());
            } else {
                Response result = Response.ok();
                if (throwable.getCause().getCause() instanceof ZuulException) {
                    if (((ZuulException) throwable.getCause().getCause()).nStatusCode == 429) {
                        result = Response.failure("接口请求太频繁，请稍后访问！", 429);
                        log.error("errorFilter过滤，异常原因：{}", result);
                    }
                }
                request.setAttribute("exception", result);
                response.getOutputStream().write(result.toString().getBytes());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ReflectionUtils.rethrowRuntimeException(ex);
        }
        return null;
    }

    @Override
    public String filterType() {
        return FilterConstants.ERROR_TYPE;
    }

    @Override
    public int filterOrder() {
        return -1;
    }
}
