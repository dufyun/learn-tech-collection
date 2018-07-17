package org.dufy.listener;

import javax.annotation.Resource;

import org.dufy.BaseCaseTest;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

/**
 * 测试 Spring listener 的测试类
 *
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/7/17
 */
public class DemoListenerTest extends BaseCaseTest {

    @Resource
    private ApplicationContext context;

    @Test
    public void testDemoListener(){
        /**
         * 验证 Spring Listener Event
         */
        DemoPublisher demoPublisher = new DemoPublisher(context);
        demoPublisher.publishEvent(new DemoEvent(context,"test listener..."));
    }

    @Test
    public void testMyListener(){
        /**
         * 验证 MyListener ，只要spring容器启动就会执行力此方法！
         */
    }
}
