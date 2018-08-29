package org.dufy.exception;

/**
 * 服务异常抽象类 ，定义公共的异常消息提示信息！
 *
 * 具体业务可以继承这个类，自定义业务异常！
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/8/29
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
public class BaseServiceException extends RuntimeException{

    /**
     * 默认构造
     */
    public BaseServiceException() {
        super();
    }

    /**
     * @param msg 异常消息
     */
    public BaseServiceException(String msg) {
        super(msg);
    }


    /**
     * @param errCode 自定义 错误编码
     * @param msg     自定义错误信息
     */
    public BaseServiceException(String errCode, String msg) {
        super("errCode:" + errCode + ",errMsg:" + msg);
    }

}
