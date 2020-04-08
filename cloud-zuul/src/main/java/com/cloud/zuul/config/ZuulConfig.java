package com.cloud.zuul.config;

import com.cloud.zuul.filter.AuthFilter;
import com.cloud.zuul.filter.RedirectFilter;
import com.cloud.zuul.route.ZkRouteLoad;
import com.netflix.zuul.ZuulFilter;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Configuration
public class ZuulConfig {

    @Autowired
    private ZuulProperties zuulProperties;

    @Autowired
    private ServerProperties server;

    @Bean
    public ZuulFilter authFilter() {
        return new AuthFilter();
    }

    @Bean
    public ZuulFilter redirectFilter() {
        return new RedirectFilter();
    }

    @Bean("routeLocator")
    public ZkRouteLoad routeLocator() {
        ZkRouteLoad routeLocator = new ZkRouteLoad(this.server.getServlet().getContextPath(), this.zuulProperties);
        return routeLocator;
    }

}
