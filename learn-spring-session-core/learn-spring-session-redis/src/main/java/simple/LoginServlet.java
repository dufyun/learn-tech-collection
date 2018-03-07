package simple;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于登录测试
 *
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/3/1
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        request.getSession().setAttribute("testKey", "742981086@qq.com");

        request.getSession().setMaxInactiveInterval(10*1000);

        response.sendRedirect(request.getContextPath() + "/session");

    }

}
