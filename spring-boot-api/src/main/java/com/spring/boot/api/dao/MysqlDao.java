package com.spring.boot.api.dao;

import com.spring.boot.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MysqlDao {

    /**
     * 接口绑定，通过方法上的注解配置sql语句实现（sql简单的情况下，使用注解绑定）
     */
    @Select("select * from user where id=#{id}")
    User getUserById(Long id);

}
