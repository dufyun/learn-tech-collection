package org.dufy;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * 启动类
 *
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/7/16
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
public class AppMain {

    private static ApplicationContext context;

    public static void main(String[] args) {
        GenericXmlApplicationContext context = new GenericXmlApplicationContext();
        context.load("classpath*:spring-config/spring*.xml");
        setContext(context);
        context.refresh();
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static void setContext(ApplicationContext context) {
        AppMain.context = context;
    }
}
