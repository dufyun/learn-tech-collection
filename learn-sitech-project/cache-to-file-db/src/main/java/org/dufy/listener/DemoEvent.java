package org.dufy.listener;

import org.springframework.context.ApplicationEvent;

/**
 * 测试的Event
 *
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/7/17
 */
public class DemoEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public DemoEvent(Object source) {
        super(source);
    }

    private String msg;

    public void sysLog() {
        System.out.println(msg);
    }

    public DemoEvent(Object source,String msg) {
        super(source);
        this.setMsg(msg);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
