package com.cloud.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 *
 *
 * @version 1.0
 * @date 2020/4/15 11:13
 */
@Slf4j
public class ErrorFilter extends ZuulFilter {


    @Override
    public String filterType() {
        return FilterConstants.ERROR_TYPE;
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        log.info("[zuul]error过滤");
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletResponse response = ctx.getResponse();
        response.setStatus(500);
        response.setHeader("Content-Type", "text/html;charset=UTF-8");
        try {
            response.getWriter().write("服务不可用，请稍后重试.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
