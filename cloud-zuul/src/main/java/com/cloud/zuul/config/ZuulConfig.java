package com.cloud.zuul.config;

import com.cloud.zuul.filter.PreFilter;
import com.cloud.zuul.filter.ErrorFilter;
import com.cloud.zuul.filter.PostFilter;
import com.cloud.zuul.filter.RouteFilter;
import com.cloud.zuul.route.ZkRouteLoad;
import com.netflix.zuul.ZuulFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author
 * @version 1.0
 * @date 2020/3/31 17:17
 */
@Slf4j
@Configuration
public class ZuulConfig {

    @Autowired
    private ZuulProperties zuulProperties;

    @Autowired
    private ServerProperties server;

    @Bean
    public ZuulFilter authFilter() {
        return new PreFilter();
    }

    @Bean
    public ZuulFilter redirectFilter() {
        return new RouteFilter();
    }

    @Bean
    public ZuulFilter postFilter() {
        return new PostFilter();
    }

    @Bean
    public ErrorFilter errorFilter() {
        return new ErrorFilter();
    }

    @Bean("routeLocator")
    public ZkRouteLoad routeLocator() {
        ZkRouteLoad routeLocator = new ZkRouteLoad(this.server.getServlet().getContextPath(), this.zuulProperties);
        return routeLocator;
    }

}
