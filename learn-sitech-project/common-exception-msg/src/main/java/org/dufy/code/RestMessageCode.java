package org.dufy.code;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Rest错误码
 *
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/8/29
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
public class RestMessageCode extends BaseMessageCode {

    private static Logger logger = LoggerFactory.getLogger(RestMessageCode.class);
    /**
     * 定义加载的message文件名称
     */
    private static final String MESSAGE_NAME = "restMessage";

    //-------------------- 定义 rest异常消息码 ： 100000--199999 -----------------//

    /**
     * 发送消息成功
     */
    public static String SEND_MSG_OK = "100000";

    public static String SEND_MSG_ERROR = "100001";


    static {
        errMsgs.putAll(initErrorMessage(MESSAGE_NAME));
    }

}
