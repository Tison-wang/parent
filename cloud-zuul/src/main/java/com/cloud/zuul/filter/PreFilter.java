package com.cloud.zuul.filter;

import com.base.common.response.Response;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;

/**
 * @author
 * @version 1.0
 * @date 2020/3/31 17:14
 */
@Slf4j
public class PreFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String url = request.getRequestURI();
        log.info("[zuul]权限验证：{}", url);
        String headerToken = request.getHeader("token");
        Object accessToken = request.getParameter("token");
        if (accessToken == null && headerToken == null) {
            ctx.setSendZuulResponse(false); // 过滤该请求，不对其进行路由
            ctx.setResponseStatusCode(401); // 返回错误码
            try {
                String result = Response.failure("登录信息为空！", 500).toString();
                ctx.setResponseBody(result);
                ctx.getResponse().setCharacterEncoding("UTF-8");
                ctx.getResponse().setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
                ctx.set("enabled", false); // 让下一个Filter看到上一个Filter的状态
                log.info("[zuul]权限验证失败：登录信息为空！");
            } catch (Exception e) {
            }
        } else {
            ctx.setSendZuulResponse(true); // 对该请求进行路由
            ctx.setResponseStatusCode(200);
            ctx.set("enabled", true);
        }
        log.info("[zuul]权限验证通过：headerToken={}，accessToken={}", headerToken, accessToken);
        return null;
    }
}
