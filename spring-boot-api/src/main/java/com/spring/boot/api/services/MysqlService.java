package com.spring.boot.api.services;

import com.spring.boot.model.User;
import com.spring.boot.model.UserVO;

import java.util.List;

public interface MysqlService {

    User getUserById(Long id);

    User getUserByName(String name);

    List<User> queryList(UserVO userVo);

    Long addUser(User user);

}
