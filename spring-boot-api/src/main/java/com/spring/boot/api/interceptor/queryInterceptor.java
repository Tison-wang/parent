//package com.spring.boot.api.interceptor;
//
//import com.alibaba.fastjson.JSON;
//import org.apache.ibatis.executor.parameter.ParameterHandler;
//import org.apache.ibatis.executor.statement.StatementHandler;
//import org.apache.ibatis.mapping.BoundSql;
//import org.apache.ibatis.plugin.*;
//import org.apache.ibatis.session.ResultHandler;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.sql.Connection;
//import java.sql.Statement;
//import java.util.Properties;
//
///**
// *
// */
//@Intercepts({ @Signature(type = StatementHandler.class, method = "query", args = { Statement.class, ResultHandler.class}),
//        @Signature(type = StatementHandler.class, method = "update", args = { Statement.class})})
//public class queryInterceptor implements Interceptor {
//
//    private final static Logger logger = LoggerFactory.getLogger(queryInterceptor.class);
//
//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//       /* StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
//        ParameterHandler paramter = statementHandler.getParameterHandler();
//        Object o = paramter.getParameterObject();
//        logger.info("【mybatis before query】:{}", JSON.toJSONString(o));*/
//        return invocation.proceed();
//    }
//
//    @Override
//    public Object plugin(Object target) {
//        return Plugin.wrap(target, this);
//    }
//
//    @Override
//    public void setProperties(Properties properties) {
//
//    }
//}
