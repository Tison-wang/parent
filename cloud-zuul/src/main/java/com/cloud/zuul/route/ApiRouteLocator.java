package com.cloud.zuul.route;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 *
 * @author
 * @version 1.0
 * @date 2020/4/3 12:14
 */
@Slf4j
public abstract class ApiRouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator {

    private ZuulProperties properties;

    public ApiRouteLocator(String servletPath, ZuulProperties properties) {
        super(servletPath, properties);
        this.properties = properties;
        log.info("servletPath:{}", servletPath);
    }

    /**
     * 刷新路由配置
     *
     * @version 1.0
     * @return
     */
    @Override
    public void refresh() {
        doRefresh();
    }

    @Override
    protected Map<String, ZuulRoute> locateRoutes() {
        LinkedHashMap<String, ZuulRoute> routesMap = new LinkedHashMap<String, ZuulRoute>();
        // 从application.properties中加载路由配置
        routesMap.putAll(super.locateRoutes());
        // 从zookeeper中加载路由配置
        routesMap.putAll(loadLocateRoute());
        // 优化一下配置
        LinkedHashMap<String, ZuulRoute> values = new LinkedHashMap<>();
        for (Map.Entry<String, ZuulRoute> entry : routesMap.entrySet()) {
            String path = entry.getKey();
            // Prepend with slash if not already present.
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (StringUtils.hasText(this.properties.getPrefix())) {
                path = this.properties.getPrefix() + path;
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
            }
            values.put(path, entry.getValue());
        }
        return values;
    }

    /**
     * @description 加载路由配置，由子类去实现
     * @version 1.0.0
     * @return
     */
    public abstract Map<String, ZuulRoute> loadLocateRoute();

    /**
     * @description 获取路由规则，由子类去实现
     * @version 1.0.0
     * @return
     */
    //public abstract List<IZuulRouteRule> getRules(Route route);

    /**
     * @description 通过配置的规则改变路由目的地址
     * @version 1.0.0
     * @return
     */
    @Override
    public Route getMatchingRoute(String path) {
        Route route = super.getMatchingRoute(path);
        // 增加自定义路由规则判断
        //List<IZuulRouteRule> rules = getRules(route);
        //return zuulRouteRuleMatcher.matchingRule(route, rules);
        return route;
    }

    /**
     * @description 路由定位器的优先级
     * @version 1.0.0
     * @return
     */
    @Override
    public int getOrder() {
        return -1;
    }

}
