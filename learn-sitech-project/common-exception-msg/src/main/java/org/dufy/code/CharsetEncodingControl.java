package org.dufy.code;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;


/**
 * 指定编码编码去读取资源控制类
 *
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/8/29
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
public class CharsetEncodingControl extends ResourceBundle.Control {


    public static final String US_ASCII = "US-ASCII";
    public static final String ISO_8859_1 = "ISO-8859-1";
    public static final String UTF_8 = "UTF-8";
    public static final String UTF_16BE = "UTF-16BE";
    public static final String UTF_16LE = "UTF-16LE";
    public static final String UTF_16 = "UTF-16";

    /**
     * 默认字符集为utf-8
     */
    private static final String default_charsetName = UTF_8;

    final String charset_name;

    public CharsetEncodingControl() {
        this(default_charsetName);
    }

    public CharsetEncodingControl(String charsetName) {
        charset_name = charsetName;
    }

    @Override
    public ResourceBundle newBundle
            (String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
            throws IllegalAccessException, InstantiationException, IOException
    {
        // The below is a copy of the default implementation.
        String bundleName = toBundleName(baseName, locale);
        String resourceName = toResourceName(bundleName, "properties");
        ResourceBundle bundle = null;
        InputStream stream = null;
        if (reload) {
            URL url = loader.getResource(resourceName);
            if (url != null) {
                URLConnection connection = url.openConnection();
                if (connection != null) {
                    connection.setUseCaches(false);
                    stream = connection.getInputStream();
                }
            }
        } else {
            stream = loader.getResourceAsStream(resourceName);
        }
        if (stream != null) {
            try {
                // Only this line is changed to make it to read properties files as charset_name.
                bundle = new PropertyResourceBundle(new InputStreamReader(stream, charset_name));
            } finally {
                stream.close();
            }
        }
        return bundle;
    }
}