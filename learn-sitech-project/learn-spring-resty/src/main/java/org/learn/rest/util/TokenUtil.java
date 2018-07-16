package org.learn.rest.util;

import org.apache.commons.codec.binary.Base64;

import cn.hutool.setting.dialect.Props;

/**
 * 生成token
 *
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/7/16
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
public class TokenUtil {

    /**
     * 根据指定的渠道的name和渠道的key生成唯一的token
     * @param channelName
     * @param channelKey
     * @return
     */
    public static String cookieToken(String channelName, String channelKey) {
        String key_token;
        String encodeName = new String(Base64.encodeBase64(channelName.getBytes()));
        key_token = channelKey + "&" + encodeName;
        return MD5Util.MD5Encode(key_token);
    }
    private static Props props = (Props) Props.getProp("cookieTokens.properties");
    public static void main(String[] args) {

        String msg = "order-center";
        String key_token = "query-order-list";
        System.out.println(cookieToken(msg, key_token));

        String configToken = props.getStr(key_token);
        System.out.println(configToken);


    }
}
