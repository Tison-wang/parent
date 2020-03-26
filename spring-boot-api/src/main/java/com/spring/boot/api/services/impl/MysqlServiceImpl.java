package com.spring.boot.api.services.impl;

import com.spring.boot.api.dao.MysqlDao;
import com.spring.boot.api.dao.MysqlDao1;
import com.spring.boot.api.services.MysqlService;
import com.spring.boot.model.User;
import com.spring.boot.model.UserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MysqlServiceImpl implements MysqlService {

    @Autowired
    private MysqlDao mysqlDao;

    @Autowired
    private MysqlDao1 mysqlDao1;

    /**
     * 根据id获取用户信息
     * @param id 主键
     * @return
     */
    @Override
    public User getUserById(Long id) {
        return mysqlDao.getUserById(id);
    }

    @Override
    public User getUserByName(@Param("name") String name) {
        return mysqlDao1.getUserByName(name);
    }

    @Override
    public List<User> queryList(UserVO userVo) {
        return mysqlDao1.queryList(userVo);
    }

    @Override
    public Long addUser(User user) {
        return mysqlDao1.addUser(user);
    }

}
