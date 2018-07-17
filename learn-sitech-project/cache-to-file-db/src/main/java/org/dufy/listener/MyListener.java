package org.dufy.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 测试Listener
 *
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/7/17
 */
@Component
public class MyListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("============Spring启动完成就执行该方法============");
    }
}
