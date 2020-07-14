package com.spring.boot.api.dao;

import com.spring.boot.api.vo.UserDetail;
import com.spring.boot.api.vo.UserVo;
import com.spring.boot.model.User;
import com.spring.boot.model.UserVO;

import java.util.List;

/**
 * xml方式绑定。类全名为xml里namespace属性值，方法名为每个查询语句标签中的id属性值
 * SQL语句比较复杂时候，用xml绑定
 */
public interface MysqlDao1 {

    User getUserByName(String name);

    List<User> queryList(UserVO userVo);

    List<UserVo> queryUserList(UserVO userVo);

    List<UserDetail> selectUserDetail(UserVO userVo);

    Long addUser(User user);

}