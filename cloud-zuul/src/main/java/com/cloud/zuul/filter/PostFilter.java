package com.cloud.zuul.filter;

import com.alibaba.fastjson.JSONObject;
import com.base.common.response.Response;
import com.base.common.utils.ObjectByteConvert;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * @author
 * @version 1.0
 * @date 2020/3/31 17:57
 */
@Slf4j
public class PostFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        Boolean enabled = ctx.get("enabled") == null ? false : (boolean) ctx.get("enabled");
        return enabled;
    }

    @Override
    public Object run() throws ZuulException {
        log.info("[zuul]POST提交");
        RequestContext ctx = RequestContext.getCurrentContext();
        try {
            InputStream stream = ctx.getResponseDataStream();
            String body = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
            Response response = JSONObject.parseObject(body, Response.class);
            log.info("post返回值：{}", body);
            if (Objects.nonNull(response)) {
                if (response.getStatus().equals(Response.FAILED_STATUS)) {
                    ctx.setSendZuulResponse(false); // 过滤该请求，不对其进行路由
                    ctx.setResponseStatusCode(401); // 返回错误码
                    ctx.setResponseBody(body);
                    throw new RuntimeException(response.getMsg());
                } else {
                    ctx.setSendZuulResponse(true); // 对该请求进行路由
                    ctx.setResponseStatusCode(200);
                    ctx.setResponseBody(body);
                    ctx.getResponse().setCharacterEncoding("UTF-8");
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return null;
    }
}
