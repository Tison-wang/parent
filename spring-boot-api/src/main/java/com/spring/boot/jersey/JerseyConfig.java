package com.spring.boot.jersey;

import com.spring.boot.exception.JerseyExceptionMapper;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

/**
 * 为resource类注册，建议一个resource类，单独的一个config类为其注册
 *
 * @author Tison.wang
 * @version 1.0
 * @date 2019/12/23 17:27
 */
@Component
@ApplicationPath("jersey")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {

        register(JerseyExceptionMapper.class);
        register(JerseyResource.class);
        register(JerseyLoadClassResource.class);

    }
}
