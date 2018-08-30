package org.dufy;

import org.dufy.code.BaseMessageCode;
import org.dufy.code.RestMessageCode;
import org.dufy.exception.BaseServiceException;
import org.dufy.exception.RestServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试自定义的 code 和 exception
 *
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/8/30
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
public class TestCustomizeExceptionMain {

    private static Logger logger = LoggerFactory.getLogger(TestCustomizeExceptionMain.class);

    public static void main(String[] args) {

        testBaseException();
//        testRestException();

    }

    /**
     * 验证 message.properties
     */
    public static void testBaseException(){

        logger.info("---------------- ---> SUCCESS : " + BaseMessageCode.SUCCESS);

        //验证获取成功消息码的内容
        logger.info("000000 ---> errMsgs : " + BaseMessageCode.getMsg(BaseMessageCode.SUCCESS));

        logger.info("errMsgs : " + BaseMessageCode.errMsgs);

        //验证出现空指针异常，动态输出错误内容

        String strNull = null;
        if(strNull == null){
            String errorCodeByException = BaseMessageCode.getErrorCodeByException(new NullPointerException());
            logger.info("020000 ---> errMsgs : "+ BaseMessageCode.getMsg(errorCodeByException,"strNull为null！"));
        }

        logger.info("java.version : " + System.getProperty("java.version"));

        //正常业务中使用，结合自定义异常使用
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        throw new BaseServiceException(BaseMessageCode.ERROR , BaseMessageCode.getMsg(BaseMessageCode.ERROR));
    }

    /**
     * 验证 restMessage.properties
     */
    public static void testRestException(){

        String msg = RestMessageCode.getMsg(RestMessageCode.SEND_MSG_OK);
        logger.info("msg : " + msg);

        //正常业务中使用，结合自定义异常使用
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        throw new RestServiceException(RestMessageCode.SEND_MSG_ERROR , RestMessageCode.getMsg(RestMessageCode.SEND_MSG_ERROR));
    }
}
