package org.learn.rest.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.learn.rest.util.CookieUtil;
import org.learn.rest.util.RetCodeConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

/**
 * 通过cookie值进行校验令牌，如果cookie值满足需求，则放过，否则不允许访问
 *
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/7/16
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
public class CookieAuthFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(CookieAuthFilter.class);

    private static Props props = (Props) Props.getProp("cookieTokens.properties");
    /**
     * 是否必须校验cookie
     * 0 不校验 1 全站校验
     */
    private int authFlag = 0;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.debug("CookieAuthFilter#init()");
        authFlag = Integer.parseInt(filterConfig.getInitParameter("authFlag"));
    }

    /**
     * notice:如果要进行cookie约束，则一定要在cookieTokens中进行配置。这是充分条件
     *
     * @param req
     * @param resp
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;


        String requestURI = request.getRequestURI();

        String cookieAuthWhiteUrl = props.getProperty("cookieAuthWhiteUrl");
        if (!StrUtil.isBlank(cookieAuthWhiteUrl)) {
            String[] strs = cookieAuthWhiteUrl.split("\\|");
            // 白名单
            for (String str : strs) {
                if (requestURI.contains(str)) {
                    chain.doFilter(request, response);
                    return;
                }
            }
        }

        String token = "";
        String targetKey = "";

        if (props.keySet().isEmpty()) {
            logger.info("cookieTokens.properties文件未作配置,请注意!!");
        }
        Cookie[] cookies = request.getCookies();

        //根据配置文件查找cookie
        for (Object key : props.keySet()) {
            token = CookieUtil.getCookieToken(cookies, key.toString().trim());

            // token不为空,查找到token值
            if (StrUtil.isNotBlank(token)) {
                targetKey = key.toString().trim();
                break;
            }
        }
        logger.debug("targetKey=======> " + targetKey);
        logger.debug("token=======>" + token);

        // token不为空,则对应的key肯定不为null
        if (StrUtil.isNotBlank(token)) {
            // 配置的tokenlist,以|分割
            String configToken = props.getStr(targetKey);
            List<String> tokens = Arrays.asList(configToken.split("\\|"));

            // 匹配成功
            if (tokens.contains(token)) {
                logger.debug("cookie认证匹配成功============> " + token);
                request.setAttribute("token", token);
                chain.doFilter(request, response);
            } else {
                constructJsonReturn(response, RetCodeConstant.COOKIE_MATCH_FAIL, "令牌错误", requestURI);
                return;
            }
        } else {

            if (authFlag == 0) {
                chain.doFilter(request, response);
                return;
            }

            if (props.keySet().isEmpty()) {
                throw new ServletException("cookieTokens.properties文件未作配置");
            }

            constructJsonReturn(response,RetCodeConstant.COOKIE_MATCH_NOT_EXISTS,
                    "令牌传递错误或者未携带令牌", requestURI);

        }
    }

    private void constructJsonReturn(HttpServletResponse res, String retCode,
                                     String retMsg, String uri) {
        JSONObject json = new JSONObject();
        json.put("retCode", retCode);
        json.put("retMsg", retMsg);

        res.setCharacterEncoding("UTF-8");
        res.setContentType("application/json; charset=utf-8");
        try {
            logger.info(uri + ",cookie认证不通过,返回:" + json);
            res.getWriter().append(json.toJSONString());
            res.flushBuffer();
            res.getWriter().close();
        } catch (IOException e) {
            logger.error("响应返回失败", e);
        }

    }

    @Override
    public void destroy() {
        logger.debug("CookieAuthFilter#destroy()");
    }
}
