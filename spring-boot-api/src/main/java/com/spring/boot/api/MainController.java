package com.spring.boot.api;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.spring.boot.api.services.MysqlService;
import com.spring.boot.model.PageVO;
import com.spring.boot.model.User;
import com.spring.boot.model.UserVO;
import com.spring.boot.utils.Response;
import com.tszk.common.api.utils.ZkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
public class MainController {

	private final static Logger logger = LoggerFactory.getLogger(MainController.class);

	@Autowired
	private MysqlService mysqlService;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private ZkUtils zkUtils;

    @RequestMapping("/")
    public String home(String name) {
        return "redirect:/static/index.html";
    }

    @RequestMapping("/zk")
    @ResponseBody
    public void test(String v){
        String path = "/zk-watcher-1";
        logger.info("zk test，data={}",v);
        zkUtils.updateNode(path, v);
    }

    @RequestMapping("/test")
    @ResponseBody
    public void test1(HttpServletResponse res){
        res.setHeader("content-type", "text/html;charset=UTF-8");
        PrintWriter out = null;
        try {
            out = res.getWriter();
            List<User> userLists = mysqlService.queryList(new UserVO());
            Thread.sleep(1000);
            out.write("输出日志：<br/>");
            out.flush();
            Thread.sleep(1000);
            out.write("总共"+userLists.size()+"条数据<br/>");
            out.flush();
            Thread.sleep(1000);
            out.write("开始打印...<br/>");
            out.flush();
            Thread.sleep(3000);
            for(User user : userLists) {
                logger.info(user.getName());
                out.write("<a>"+user.getName()+"</a><br/>");
                out.flush();
                Thread.sleep(1000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

	@RequestMapping("/hello")
	//@Cacheable(key="001")
	@ResponseBody
    public String test(@RequestBody User user) {
		logger.info("参数2：" + user.getId());
		String redisVal = redisTemplate.opsForValue().get(user.getId());
		logger.info(redisVal);
        return redisVal;
    }

	@RequestMapping(value = {"/get"})
    @ResponseBody
	public User selectById(String params) {
		logger.info("根据id查询user，param={}", params);
		User user = mysqlService.getUserById(Long.valueOf(params));
		if(null != user) {
            logger.info("id: " + user.getId());
            logger.info("name: " + user.getName());
        }
		return user;
	}



    @RequestMapping(value = {"/query"})
    @ResponseBody
    public User selectByName(String params) {
        logger.info("根据name查询user，param={}", params);
        User user = mysqlService.getUserByName(params);
        if(null != user) {
            logger.info("id: " + user.getId());
            logger.info("name: " + user.getName());
        }  else {
            throw new RuntimeException("请求数据内容为空");
        }
        return user;
    }

    @RequestMapping(value = {"/add"})
    @ResponseBody
    public User add(String name) {
        logger.info("新增用户，name={}", name);
        User user = new User();
        user.setName(name);
        mysqlService.addUser(user);
        user = mysqlService.getUserById(user.getId());
        return user;
    }

    @RequestMapping(value = {"/list"})
    @ResponseBody
    public Response<PageVO> queryList(@RequestBody UserVO userVo) {
        logger.info("查询用户列表, userVo={}", JSON.toJSONString(userVo));
        Page page = PageHelper.startPage(userVo.getPageIndex(), userVo.getPageSize(), true);
        List<User> userLists = mysqlService.queryList(userVo);
        for(User user : userLists) {
            logger.info("id:{}, name:{}", user.getId(), user.getName());
        }
        PageVO pv = PageVO.getPage(page, userLists);
        return Response.ok(pv);
    }
}
