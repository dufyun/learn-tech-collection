package com.dufy.webscocket.stomp;

import com.dufy.webscocket.entity.User;
import com.dufy.webscocket.session.WebSocketSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述该类概要功能介绍
 *
 * @author:dufy
 * @version:1.0.0
 * @date 2017/7/31
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
@Controller
public class IndexController {


    @Autowired
    private WebSocketSessionManager webSocketSessionManager;

    Map<Long, User> users = new HashMap<Long, User>();
    // 模拟一些数据
    @ModelAttribute
    public void setReqAndRes() {
        User u1 = new User();
        u1.setId(1L);
        u1.setName("张三");
        users.put(u1.getId(), u1);

        User u2 = new User();
        u2.setId(2L);
        u2.setName("李四");
        users.put(u2.getId(), u2);

    }
    @RequestMapping("/index")
    public String hello(){
        System.out.println("------------hello----------------");
        return "showName";
    }

    // 用户登录
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView doLogin(User user, HttpServletRequest request) {
        System.out.println("------------doLogin----------------");
        request.getSession().setAttribute("uid", user.getId());
        request.getSession().setAttribute("name", users.get(user.getId()).getName());
        return new ModelAndView("redirect:websocket");
    }
    @RequestMapping("/websocket")
    public String websocket(){
        System.out.println("------------websocket----------------");
        return "websocket";
    }
    @RequestMapping("/agent")
    public String agent(HttpServletRequest request){
        String wsId = webSocketSessionManager.webSocketId();
        System.out.println("------------agent----------------" + wsId);
        request.setAttribute("wsId",wsId);
        return "agent";
    }

}
