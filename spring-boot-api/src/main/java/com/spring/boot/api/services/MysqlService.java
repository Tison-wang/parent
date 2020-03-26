package com.spring.boot.api.services;

import com.spring.boot.model.User;
import com.spring.boot.model.UserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MysqlService {

    User getUserById(Long id);

    User getUserByName(@Param("name") String name);

    List<User> queryList(UserVO userVo);

    Long addUser(User user);

}
