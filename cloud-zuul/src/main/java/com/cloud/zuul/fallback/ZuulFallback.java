package com.cloud.zuul.fallback;

import com.base.common.response.Response;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author
 * @version 1.0
 * @date 2020/4/14 17:32
 */
@Slf4j
@Component
public class ZuulFallback implements FallbackProvider {

    @Override
    public String getRoute() {
        //设置熔断的服务名
        //如果是所有服务则设置为*
        return "abc";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        log.info("请求超时，启用熔断");

        if (cause != null && cause.getCause() != null) {
            String reason = cause.getCause().getMessage();
            log.info("发生异常了：{}", reason);
        }

        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return 503;
            }

            @Override
            public String getStatusText() throws IOException {
                String result = Response.failure("请求的服务不可用, 请稍后重试", 500).toString();
                log.info("熔断返回：{}", result);
                return result;
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() throws IOException {
                RequestContext ctx = RequestContext.getCurrentContext();
                ctx.set("enabled", false);
                return new ByteArrayInputStream(getStatusText().getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
    }
}