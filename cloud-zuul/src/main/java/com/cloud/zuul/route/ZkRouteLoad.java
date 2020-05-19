package com.cloud.zuul.route;

import com.base.common.utils.ObjectByteConvert;
import com.base.common.utils.SpringUtil;
import com.tszk.common.api.route.ZuulRoute;
import com.tszk.common.api.utils.ZkUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @version 1.0
 * @date 2020/4/3 17:52
 */
public class ZkRouteLoad extends ApiRouteLocator {

    public ZkRouteLoad(String servletPath, ZuulProperties properties) {
        super(servletPath, properties);
    }

    @Override
    public Map<String, ZuulProperties.ZuulRoute> loadLocateRoute() {
        Map<String, ZuulProperties.ZuulRoute> routes = new LinkedHashMap<>();
        // 从 zk 获取路由配置信息返回
        ZkUtils zkUtils = SpringUtil.getBean(ZkUtils.class);
        byte[] data = zkUtils.getDataForByte("/zk-watcher-1", null);
        List<ZuulRoute> routeList = ObjectByteConvert.toObject(data);
        routeList.stream().forEach(route -> {
            ZuulProperties.ZuulRoute zuulRoute = new ZuulProperties.ZuulRoute();
            BeanUtils.copyProperties(route, zuulRoute);
            routes.put(route.getPath(), zuulRoute);
        });
        return routes;
    }
}
