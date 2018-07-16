package org.learn.rest.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.learn.rest.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;

/**
 * demo演示类
 *
 * @author:dufy
 * @version:1.0.0
 * @date 2017/8/15
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
@Controller
@Path("/service")
@Consumes(value= MediaType.APPLICATION_JSON)
@Produces(value=MediaType.APPLICATION_JSON)
public class DemoMessageResource {

    private static final Logger logger = LoggerFactory.getLogger(DemoMessageResource.class);

    @POST
    @Path("/msg")
    public String unReadMsg(User user){
        JSONObject json = new JSONObject();
        String name = user.getName();
        logger.info("logger ---> " + name);

        json.put("retMsg","success");
        json.put("name",name);

        return json.toJSONString();
    }

    /**
     * accept 访问的时候在filter直接被放行
     * @param user
     * @return
     */
    @POST
    @Path("/accept")
    public String accept(User user){
        String name = user.getName();
        logger.info("logger ---> " + name);

        JSONObject json = new JSONObject();
        json.put("retMsg","success");
        json.put("acceptName",name);

        return json.toJSONString();
    }

}
