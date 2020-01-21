package com.spring.boot.api.interceptor;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Properties;

/**
 *  记录执行的sql语句
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class}) })
public class SQLStatsInterceptor implements Interceptor {

    private final static Logger logger = LoggerFactory.getLogger(SQLStatsInterceptor.class);

    private static Boolean showSql = false;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if(showSql) {
            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
            BoundSql boundSql = statementHandler.getBoundSql();
            String sql = boundSql.getSql();
            logger.info("【mybatis intercept sql】:{}", sql.replaceAll("\n", " ").replaceAll("\\s+", " "));
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        String dialect = properties.getProperty("dialect");
        showSql = Boolean.valueOf(properties.getProperty("show_sql"));
        logger.info("【mybatis intercept dialect】：{}", dialect);
        logger.info("【mybatis intercept showSql】：{}", showSql);
    }
}
