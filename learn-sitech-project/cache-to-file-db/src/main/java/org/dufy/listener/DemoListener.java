package org.dufy.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 测试的Listener
 *
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/7/17
 */
@Component
public class DemoListener implements ApplicationListener<DemoEvent> {

    @Override
    public void onApplicationEvent(DemoEvent event) {
        String msg = event.getMsg();
        System.out.println("demoListener接受到了demoPublisher发布的消息："+msg);
    }
}
