package simple;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于Session测试
 *
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/3/1
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
@WebServlet("/session")
public class SessionServlet extends HttpServlet {

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        System.out.println(request.getRemoteAddr());
        System.out.print(request.getRemoteHost() + " : " + request.getRemotePort());

        String sesssionID = request.getSession().getId();
        System.out.println("-----------tomcat---sesssionID-------" + sesssionID);

        String testKey = (String)request.getSession().getAttribute("testKey");
        System.out.println("-----------tomcat-testKey-------" + testKey);

        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append("tomcat ---- sesssionID : " + sesssionID + "\n");
            out.append("{\"name\":\"dufy\"}" + "\n");
            out.append("tomcat ----- testKey : " + testKey);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(out != null){
                out.close();
            }
        }

    }

}
