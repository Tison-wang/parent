package com.spring.boot.api.dao;

import com.spring.boot.model.User;
import com.spring.boot.model.UserVO;

import java.util.List;

public interface MysqlDao1 {

    User getUserByName(String name);

    List<User> queryList(UserVO userVo);

    Long addUser(User user);

}