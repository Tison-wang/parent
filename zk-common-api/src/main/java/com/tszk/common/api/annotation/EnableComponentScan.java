package com.tszk.common.api.annotation;

import com.tszk.common.api.config.LoansConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 让 spring 容器扫描 LoansConfig 类中配置的包路径
 *
 * @author
 * @version 1.0
 * @date 2020/3/25 16:52
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import({LoansConfig.class})
public @interface EnableComponentScan {
}

