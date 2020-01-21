package com.spring.boot.api.dao;

import com.spring.boot.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MysqlDao {

    @Select("select * from user where id=#{id}")
    User getUserById(Long id);

}
