package com.spring.boot.jersey;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.spring.boot.api.services.MysqlService;
import com.spring.boot.model.PageVO;
import com.spring.boot.model.User;
import com.spring.boot.model.UserVO;
import com.spring.boot.utils.Response;
import com.tsmq.api.dto.ObjectEntity;
import com.tsmq.api.producer.AcMqProducer;
import com.tsmq.api.producer.RaMqProducer;
import com.tsmq.api.utils.ObjectByteConvert;
import com.tszk.common.api.utils.ZkUtils;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * jersey controller类
 *
 * @author Tison
 * @version 1.0
 * @date 2019/12/23 17:45
 */
@Component
@Path("resource")
public class JerseyResource {

    private final static Logger logger = LoggerFactory.getLogger(JerseyResource.class);

    @Autowired
    private MysqlService mysqlService;

    @Context
    private HttpServletRequest request;

    @Autowired
    private AcMqProducer acMqProducer;

    @Autowired
    private RaMqProducer raMqProducer;

    @Autowired
    private ZkUtils zkUtils;

    /**
     * 请求地址示例: http://localhost:8083/springboot/jersey/resource/user/张三?age=28
     */
    @GET
    @Path("user/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("name") String name, @QueryParam("name") String qname) {
        logger.info("[GET]---------------------------------------------");
        logger.info("[GET]-请求参数：name={}, qname={}", name, qname);
        logger.info("[GET]---------------------------------------------");
        /*String path = "/zk-watcher-1";
        logger.info("zk test，data={}",name);
        zkUtils.updateNode(path, name);*/
        ObjectEntity obj = ObjectEntity.builder().userName(name).age(28).build();
        acMqProducer.sendMessage(new ActiveMQQueue("activemq.test.queue"), ObjectByteConvert.toByteArray(obj));
        acMqProducer.sendMessage(new ActiveMQTopic("activemq.test.topic"), ObjectByteConvert.toByteArray(obj));
        raMqProducer.sendMessage(obj);
        User user = mysqlService.getUserByName(name);
        if(null != user) {
            logger.info("id: {}", user.getId());
            logger.info("name: {}", user.getName());
            return Response.ok(user);
        } else {
            throw new RuntimeException("请求数据内容为空");
        }
        //return Response.failure(true,"请求数据内容为空", 400, null);
    }

    /**
     * 请求地址示例: http://localhost:8083/springboot/jersey/resource/user
     */
    @POST
    @Path("user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response<PageVO> queryUsers(@BeanParam UserVO vo) {
        logger.info("[POST]-请求参数：vo={}", JSONObject.toJSONString(vo));
        Page page = PageHelper.startPage(vo.getPageIndex(), vo.getPageSize(), true);
        List<User> userLists = mysqlService.queryList(vo);
        userLists.forEach(user -> logger.info("id:{}, name:{}", user.getId(), user.getName()));
        PageVO pv = PageVO.getPage(page, userLists);
        /*String path = "/zk-watcher-2";
        logger.info("zk test，data={}",vo.getName());
        zkUtils.updateNode(path, vo.getName());*/
        return Response.ok(pv);
    }

    @PUT
    @Path("user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateUser(@BeanParam UserVO vo) {
        logger.info("[PUT]-请求参数：vo={}", JSONObject.toJSONString(vo));
        return Response.ok("update success");
    }

    @DELETE
    @Path("user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("userId") Integer userId) {
        logger.info("[DELETE]-请求参数：userId={}", userId);
        return Response.ok("delete success");
    }

}
