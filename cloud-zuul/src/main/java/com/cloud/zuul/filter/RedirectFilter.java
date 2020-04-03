package com.cloud.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import javax.servlet.http.HttpServletRequest;

/**
 *
 *
 * @author
 * @version 1.0
 * @date 2020/3/31 17:57
 */
@Slf4j
public class RedirectFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.ROUTE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        log.info("[zuul Url重定向]");
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String url = ctx.get(FilterConstants.REQUEST_URI_KEY).toString();
        url = url.replaceAll("test", "詹淘朗");
        ctx.put(FilterConstants.REQUEST_URI_KEY, url);
        log.info("[zuul]修改后请求地址：{}", url);
        return null;
    }
}
