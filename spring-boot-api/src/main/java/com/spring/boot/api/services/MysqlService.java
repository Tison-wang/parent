package com.spring.boot.api.services;

import com.spring.boot.api.vo.UserDetail;
import com.spring.boot.api.vo.UserVo;
import com.spring.boot.model.User;
import com.spring.boot.model.UserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MysqlService {

    User getUserById(Long id);

    User getUserByName(@Param("name") String name);

    List<User> queryList(UserVO userVo);

    List<UserVo> queryUserList(UserVO userVo);

    List<UserDetail> selectUserDetail(UserVO userVo);

    Long addUser(User user);

}
