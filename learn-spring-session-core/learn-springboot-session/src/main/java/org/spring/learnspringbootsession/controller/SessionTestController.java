package org.spring.learnspringbootsession.controller;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring boot 测试session共享Controller
 *
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/3/18
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
@RestController
public class SessionTestController {

    @RequestMapping("/uid")
    String uid(HttpSession session) {
        UUID uid = (UUID) session.getAttribute("uid");
        if (uid == null) {
            uid = UUID.randomUUID();
        }
        session.setAttribute("uid", uid);
        return session.getId();
    }
}
