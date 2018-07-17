package org.dufy.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

/**
 * 测试的事件发布
 *
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/7/17
 */
public class DemoPublisher {

    private final ApplicationEventPublisher eventPublisher;

    private static final Logger logger = LoggerFactory.getLogger( DemoPublisher.class );

    public DemoPublisher( ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
    public void publishEvent(ApplicationEvent event) {
        try {
            eventPublisher.publishEvent(event);
        }
        catch (Throwable ex) {
            //忽略异常
            logger.error( "Failed to publisher event.", ex );
        }
    }


}
