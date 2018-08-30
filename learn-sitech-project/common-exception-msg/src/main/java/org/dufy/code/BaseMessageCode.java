package org.dufy.code;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息code 抽象类
 * 具体的业务可以继承此类，并自定义消息码！
 *
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/8/29
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
public class BaseMessageCode {

    private static Logger logger = LoggerFactory.getLogger(BaseMessageCode.class);

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * 定义加载的message文件名称
     */
    private static final String MESSAGE_NAME = "message";

    private static final String tipMsg = "此code未配置提示消息！";

    /**
     * 定义默认的字符集编码
     */
    private static String CHARACTER_ENCODING = DEFAULT_CHARSET.toString();


    public static Map<String, String> errMsgs;

    // 定义通过异常码 获取异常提示信息接口


    //-------------------- 定义 公共异常消息码 ： 000000--009999 -----------------//

    /**
     * 成功
     */
    public static String SUCCESS = "000000";
    /**
     * 系统未知错误
     */
    public static String ERROR = "000001";


    //-------------------- 定义 参数校验异常消息码： 010000--019999 -----------------//

    // 自定义即可

    //-------------------- 定义 常用异常消息码： 020000--029999 -----------------//

    /**
     * 空指针异常，异常提示信息：发生空指针异常，异常信息:{0}
     */
    public static String NULLPOINTEXCEPTION = "020000";

    static {
        errMsgs = initErrorMessage(MESSAGE_NAME);
    }

    /**
     * 初始化错误信息,从资源文件中读取错误信息映射关系
     * @return
     */
    public static Map<String, String> initErrorMessage(String messageName){

        Map<String, String> emsgs = new HashMap<>(64);

        ResourceBundle resourceBundle = ResourceBundle.getBundle(messageName, new CharsetEncodingControl());
        Enumeration<String> resourceBundleKeys = resourceBundle.getKeys();

        while (resourceBundleKeys.hasMoreElements()){
            String code = (String)resourceBundleKeys.nextElement();
            String msg = resourceBundle.getString(code);
            emsgs.put(code, msg);
        }

        return emsgs;

    }

    // 定义加载属性资源的接口
    /**
     * 根据错误码获取错误消息
     * @param code
     * @return 错误消息
     */
    public static String getMsg(String code){

        String msg = errMsgs.get(code);
        if(msg == null){
            return tipMsg;
        }

        return msg;
    }


    /**
     * 根据错误码获取定制的错误提示消息
     * @param code
     * @param args 用于给{0}..赋值
     * @return 错误消息
     */
    public static String getMsg(String code,Object...args){

        String msg = errMsgs.get(code);
        if(msg == null){
            return tipMsg;
        }
        return MessageFormat.format(msg,args);
    }




    /**
     * 根据异常类得到 相应的错误编码
     * @param ex  <Exception> 异常类
     * @return 错误编码<String>
     */
    public static String getErrorCodeByException(Exception ex) {
        String errorCode = ERROR;

        // 获取系统异常码
        if (ex instanceof NullPointerException) {
            errorCode = NULLPOINTEXCEPTION;
        }else if(ex.getClass().equals(NullPointerException.class)){
            errorCode = NULLPOINTEXCEPTION;
        }
        return errorCode;
    }


}
