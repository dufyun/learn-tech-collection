package org.dufy;

import org.dufy.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试 PropertiesUtil 主类
 *
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/8/29
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
public class TestPropertiesUtilMain {

    private static Logger logger = LoggerFactory.getLogger(TestPropertiesUtilMain.class);

    public static void main(String[] args) {
        by_stream();

        by_resource_bundle_default();

        by_resource_bundle();

        by_stream_resource_bundle();

        by_class();

        by_classloader();

        by_system_classloader();
    }


    /***
     * BY_STREAM = 1
     * 测试 BY_STREAM_RESOURCE_BUNDLE 获取properties内容， 指定properties所在的路径为:
     * F:\stream_resource.propertie
     */
    public static void by_stream(){
        String by_stream = PropertiesUtil.getMessage("F:\\stream_resource.propertie", "helllo",PropertiesUtil.BY_STREAM);
        logger.info("by_stream ---> hello :" + by_stream);
    }

    /**
     * BY_RESOURCE_BUNDLE = 2
     * 测试 BY_RESOURCE_BUNDLE 获取properties内容
     * config.properties
     */
    public static void by_resource_bundle_default(){
        String confMsg = PropertiesUtil.getMessage( "name");
        logger.info("by_resource_bundle ---->  confMsg : " + confMsg);
    }

    /**
     * BY_RESOURCE_BUNDLE = 2
     * 测试 BY_RESOURCE_BUNDLE 获取properties内容， 指定properties属性加载
     * message.properties
     */
    public static void by_resource_bundle(){
        String message = PropertiesUtil.getMessage("message", "000000");
        logger.info("by_resource_bundle ----->  message : " + message);
    }

    /***
     * BY_STREAM_RESOURCE_BUNDLE = 3
     * 测试 BY_STREAM_RESOURCE_BUNDLE 获取properties内容， 指定properties所在的路径为:
     * F:\stream_resource.propertie
     */
    public static void by_stream_resource_bundle(){
        String by_stream_resource_bundle = PropertiesUtil.getMessage("F:\\stream_resource.propertie", "helllo",PropertiesUtil.BY_STREAM_RESOURCE_BUNDLE);
        logger.info("by_stream_resource_bundle ---> hello :" + by_stream_resource_bundle);
    }

    /**
     * BY_CLASS = 4
     * 测试 BY_CLASS 获取properties内容，properties需要和PropertiesUtil 类在同一个包下！
     * 注  需要在pom中配置resource过滤properties
     * class.properties
     */
    public static void by_class(){
        String by_class = PropertiesUtil.getMessage("/org/dufy/util/class.properties", "classname",PropertiesUtil.BY_CLASS);
        logger.info("by_class ---> className : " + by_class);

    }

    /**
     * BY_CLASSLOADER = 5
     * 测试 BY_CLASSLOADER 获取properties内容
     * classloader.properties
     */
    public static void by_classloader(){
        String by_classloader = PropertiesUtil.getMessage("classloader.properties", "classname",PropertiesUtil.BY_CLASSLOADER);
        logger.info("by_classloader------->classLoaderName : " + by_classloader);
    }

    /**
     *  BY_SYSTEM_CLASSLOADER = 6
     * 测试 BY_SYSTEM_CLASSLOADER 获取properties内容
     * sysclassloader.properties
     */
    public static void by_system_classloader(){
        String by_system_classloader = PropertiesUtil.getMessage("sysclassloader.properties", "classname",PropertiesUtil.BY_SYSTEM_CLASSLOADER);
        logger.info("by_system_classloader ------ > sysclassLoaderName : " + by_system_classloader);
    }
}
