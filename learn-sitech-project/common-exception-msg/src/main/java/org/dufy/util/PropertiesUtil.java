package org.dufy.util;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 读取Properties文件的工具类
 *
 * @author dufy
 * @date 2018/08/29
 */
public class PropertiesUtil {

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    /**
     * 使用流读取properties属性内容
     */
    public final static int BY_STREAM = 1;
    /**
     * 使用ResourceBundle 读取properties属性内容
     */
    public final static int BY_RESOURCE_BUNDLE = 2;
    /**
     * 使用流读取文件，然后使用 PropertyResourceBundle获取属性内容
     */
    public final static int BY_STREAM_RESOURCE_BUNDLE = 3;
    /**
     * 在此类所在包下获取 properties 属性内容
     */
    public final static int BY_CLASS = 4;
    /**
     * 在此类的类路径classpath下获取properties 属性内容
     */
    public final static int BY_CLASSLOADER = 5;
    /**
     *  使用系统的类加载器加载类路径下的properties属性内容
     */
    public final static int BY_SYSTEM_CLASSLOADER = 6;

    /**
     * 默认加载和读取 为 config.properties 中的内容
     */
    public static final String DEFAULT_CONFIG_PROPERTIES = "config";

    public static  String CONF_PROPERTIES = DEFAULT_CONFIG_PROPERTIES;


    private final static Properties loadProperties(final String name,final int type) throws IOException {
        Properties p = new Properties();

        InputStream in = null;
        if (type == BY_STREAM) {

            in = new BufferedInputStream(new FileInputStream(name));
            assert (in != null);
            p.load(in);

        } else if (type == BY_RESOURCE_BUNDLE) {

            ResourceBundle rb = ResourceBundle.getBundle(name, Locale.getDefault());
            assert (rb != null);
            p = new ResourceBundleAdapter(rb);

        } else if (type == BY_STREAM_RESOURCE_BUNDLE) {

            in = new BufferedInputStream(new FileInputStream(name));
            assert (in != null);
            ResourceBundle rb = new PropertyResourceBundle(in);
            p = new ResourceBundleAdapter(rb);

        } else if (type == BY_CLASS) {

            assert (PropertiesUtil.class.equals(new PropertiesUtil().getClass()));
            in = PropertiesUtil.class.getResourceAsStream(name);
            assert (in != null);
            p.load(in);

        } else if (type == BY_CLASSLOADER) {
            assert (PropertiesUtil.class.getClassLoader().equals(new PropertiesUtil().getClass().getClassLoader()));
            in = PropertiesUtil.class.getClassLoader().getResourceAsStream(name);
            assert (in != null);
            p.load(in);

        } else if (type == BY_SYSTEM_CLASSLOADER) {
            in = ClassLoader.getSystemResourceAsStream(name);
            assert (in != null);
            p.load(in);
        }
        if (in != null) {
            in.close();
        }
        return p;
    }

    /**
     * 根据PropertiesUtil.BY_RESOURCE_BUNDLE方式从默认config.properties中去加载属性文件，
     * 并根据key获取配置内容
     * @param key
     * @return
     */
    public static String getMessage(String key) {
        Properties properties = new Properties();
        try {
            properties = PropertiesUtil.loadProperties(CONF_PROPERTIES,PropertiesUtil.BY_RESOURCE_BUNDLE);
        } catch (IOException e) {
            logger.error(CONF_PROPERTIES + ".properties not found");
        }
        if (properties.getProperty(key) == null) {
            return null;
        }
        return properties.getProperty(key).toString();
    }

    /**
     * 根据PropertiesUtil.BY_RESOURCE_BUNDLE方式从指定的ropertiesFileName.properties中去加载属性文件，
     * 并根据key获取配置内容
     * @param propertiesFileName
     * @param key
     * @return
     */
    public static String getMessage(String propertiesFileName,String key) {
        if(propertiesFileName == null || propertiesFileName == ""){
            throw new IllegalArgumentException("propertiesFileName is null!");
        }

        Properties properties = new Properties();
        try {
            properties = PropertiesUtil.loadProperties(propertiesFileName,
                    PropertiesUtil.BY_RESOURCE_BUNDLE);
        } catch (IOException e) {
            logger.error(propertiesFileName + ".properties not found");
        }
        if (properties.getProperty(key) == null) {
            return null;
        }
        return properties.getProperty(key).toString();
    }

    /**
     * 根据指定的加载方式从指定的ropertiesFileName.properties中去加载属性文件，
     * 并根据key获取配置内容
     * @param propertiesFileName
     * @param key
     * @param byType 加载properties的方式
     * @return
     */
    public static String getMessage(String propertiesFileName,String key,int byType) {
        if(propertiesFileName == null || propertiesFileName == ""){
            throw new IllegalArgumentException("propertiesFileName is null!");
        }

        Properties properties = new Properties();
        try {
            properties = PropertiesUtil.loadProperties(propertiesFileName,byType);
        } catch (IOException e) {
            logger.error(propertiesFileName + ".properties not found");
        }
        if (properties.getProperty(key) == null) {
            return null;
        }
        return properties.getProperty(key).toString();
    }

    /*
     * public static Properties loadProperties(ServletContext context, String
     * path) throws IOException { assert (context != null); InputStream in =
     * context.getResourceAsStream(path); assert (in != null); Properties p =
     * new Properties(); p.load(in); in.close(); return p; }
     */

    public static class ResourceBundleAdapter extends Properties {
        private static final long serialVersionUID = 1L;

        private ResourceBundle rb = null;

        @SuppressWarnings("unchecked")
        public ResourceBundleAdapter(ResourceBundle rb) {
            assert (rb instanceof PropertyResourceBundle);
            this.rb = rb;
            Enumeration e = rb.getKeys();
            while (e.hasMoreElements()) {
                Object o = e.nextElement();
                this.put(o, rb.getObject((String) o));
            }
        }

        public ResourceBundle getBundle(String baseName) {
            return ResourceBundle.getBundle(baseName);
        }

        public ResourceBundle getBundle(String baseName, Locale locale) {
            return ResourceBundle.getBundle(baseName, locale);
        }

        public ResourceBundle getBundle(String baseName, Locale locale,
                                        ClassLoader loader) {
            return ResourceBundle.getBundle(baseName, locale, loader);
        }

        public static String getMessage(String key) {
            Properties properties = new Properties();
            try {
                properties = PropertiesUtil.loadProperties(CONF_PROPERTIES,
                        PropertiesUtil.BY_RESOURCE_BUNDLE);
            } catch (IOException e) {
            }
            if (properties.getProperty(key) == null) {
                return null;
            }
            return properties.getProperty(key).toString();
        }

        @SuppressWarnings("unchecked")
        public Enumeration getKeys() {
            return rb.getKeys();
        }

        public Locale getLocale() {
            return rb.getLocale();
        }

        public Object getObject(String key) {
            return rb.getObject(key);
        }

        public String getString(String key) {
            return rb.getString(key);
        }

        public String[] getStringArray(String key) {
            return rb.getStringArray(key);
        }

        protected Object handleGetObject(String key) {
            return ((PropertyResourceBundle) rb).handleGetObject(key);
        }

    }

}