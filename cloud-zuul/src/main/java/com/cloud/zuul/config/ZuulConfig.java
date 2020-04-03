package com.cloud.zuul.config;

import com.cloud.zuul.filter.AuthFilter;
import com.cloud.zuul.filter.RedirectFilter;
import com.netflix.zuul.ZuulFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 *
 * @author
 * @version 1.0
 * @date 2020/3/31 17:17
 */
@Configuration
public class ZuulConfig {

    @Autowired
    ZuulProperties zuulProperties;

    @Autowired
    ServerProperties server;

    @Bean
    public ZuulFilter authFilter() {
        return new AuthFilter();
    }

    @Bean
    public ZuulFilter redirectFilter() {
        return new RedirectFilter();
    }

}
