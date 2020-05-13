package com.cloud.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletResponse;

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
            log.info("error错误信息:", throwable);
            HttpServletResponse response = ctx.getResponse();
            response.setStatus(500);
            response.setCharacterEncoding("UTF-8");
            response.getOutputStream().write(ctx.getResponseBody().getBytes());
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
