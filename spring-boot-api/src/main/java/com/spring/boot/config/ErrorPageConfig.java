package com.spring.boot.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * Description:  TODO
 *
 * @author Tison
 * @version 1.0
 * @date 2019/12/27 10:28
 */
@Configuration
public class ErrorPageConfig implements ErrorPageRegistrar {

    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        ErrorPage error401Page=new ErrorPage(HttpStatus.UNAUTHORIZED,"/index.html");
        registry.addErrorPages(error401Page);
    }
}
