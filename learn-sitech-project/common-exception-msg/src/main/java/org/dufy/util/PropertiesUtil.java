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

    private static Logger log = LoggerFactory.getLogger(PropertiesUtil.class);

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
     * 在此类的类路径下获取properties 属性内容
     */
    public final static int BY_CLASSLOADER = 5;
    /**
     *  使用系统的类加载器加载类路径下的properties属性内容
     */
    public final static int BY_SYSTEM_CLASSLOADER = 6;



    public static final String CONF_PROPERTIES = "config";


    public static final String OPEN_PROPERTIES = "conf";

    public final static Properties loadProperties(final String name,final int type) throws IOException {
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

    /*
     * public static Properties loadProperties(ServletContext context, String
     * path) throws IOException { assert (context != null); InputStream in =
     * context.getResourceAsStream(path); assert (in != null); Properties p =
     * new Properties(); p.load(in); in.close(); return p; }
     */

    public static class ResourceBundleAdapter extends Properties {
        private static final long serialVersionUID = 1L;

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

        private ResourceBundle rb = null;

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
                properties = PropertiesUtil.loadProperties(OPEN_PROPERTIES,
                        PropertiesUtil.BY_RESOURCE_BUNDLE);
            } catch (IOException e) {
                // log.error(OPEN_PROPERTIES+".properties not found");
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

    public static String getMessage(String propertiesFileName,String key) {
        Properties properties = new Properties();
        try {
            properties = PropertiesUtil.loadProperties(propertiesFileName,
                    PropertiesUtil.BY_RESOURCE_BUNDLE);
        } catch (IOException e) {
            log.error(CONF_PROPERTIES + ".properties not found");
        }
        if (properties.getProperty(key) == null) {
            return null;
        }
        return properties.getProperty(key).toString();
    }

    public static String getMessage(String key) {
        Properties properties = new Properties();
        try {
            properties = PropertiesUtil.loadProperties(CONF_PROPERTIES,
                    PropertiesUtil.BY_RESOURCE_BUNDLE);
        } catch (IOException e) {
            log.error(CONF_PROPERTIES + ".properties not found");
        }
        if (properties.getProperty(key) == null) {
            return null;
        }
        return properties.getProperty(key).toString();
    }

}