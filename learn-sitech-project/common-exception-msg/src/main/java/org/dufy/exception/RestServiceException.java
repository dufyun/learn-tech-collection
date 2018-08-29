package org.dufy.exception;

import org.dufy.code.RestMessageCode;

/**
 * Rest 异常类
 *
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/8/29
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
public class RestServiceException extends BaseServiceException {


    public RestServiceException(String errCode) {
        super(errCode, RestMessageCode.getMsg(errCode));
    }

    public RestServiceException(String errCode, String errMsg) {
        super(errCode, errMsg);
    }

    public RestServiceException(String errCode, Object...args) {
        super(errCode, RestMessageCode.getMsg(errCode,args));
    }


}
