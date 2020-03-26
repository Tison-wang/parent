package com.spring.boot.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.spring.boot.api.dao")
@ComponentScan(basePackages={"com.spring.boot"})
public class SpringBootApiApplication extends SpringBootServletInitializer {

    /**
     * 使用下面的方法为 resource 类注册
     */
    /*@Bean
    public ResourceConfig resourceConfig() {
        ResourceConfig config = new ResourceConfig();
        config.register(JerseyResource.class);
        return config;
    }*/

    /*@Bean
    public ResourceConfig resourceConfig() {
        return new ResourceConfig();
    }*/

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SpringBootApiApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootApiApplication.class, args);
    }

}