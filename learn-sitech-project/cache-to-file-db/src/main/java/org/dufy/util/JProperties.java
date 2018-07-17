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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 使用J2SE API 读取Properties文件的六种方法
 * 
 * @author xiongyw
 * 
 */
public class JProperties {
	private static Log log = LogFactory.getLog(JProperties.class);
	public static final String CONF_PROPERTIES = "config";

	public final static int BY_PROPERTIES = 1;
	public final static int BY_RESOURCEBUNDLE = 2;
	public final static int BY_PROPERTYRESOURCEBUNDLE = 3;
	public final static int BY_CLASS = 4;
	public final static int BY_CLASSLOADER = 5;
	public final static int BY_SYSTEM_CLASSLOADER = 6;
	public static final String OPEN_PROPERTIES = "conf";

	public final static Properties loadProperties(final String name,
			final int type) throws IOException {
		Properties p = new Properties();
		InputStream in = null;
		if (type == BY_PROPERTIES) {
			in = new BufferedInputStream(new FileInputStream(name));
			assert (in != null);
			p.load(in);
		} else if (type == BY_RESOURCEBUNDLE) {
			ResourceBundle rb = ResourceBundle.getBundle(name, Locale
					.getDefault());
			assert (rb != null);
			p = new ResourceBundleAdapter(rb);
		} else if (type == BY_PROPERTYRESOURCEBUNDLE) {
			in = new BufferedInputStream(new FileInputStream(name));
			assert (in != null);
			ResourceBundle rb = new PropertyResourceBundle(in);
			p = new ResourceBundleAdapter(rb);
		} else if (type == BY_CLASS) {
			assert (JProperties.class.equals(new JProperties().getClass()));
			in = JProperties.class.getResourceAsStream(name);
			assert (in != null);
			p.load(in);
			// return new JProperties().getClass().getResourceAsStream(name);
		} else if (type == BY_CLASSLOADER) {
			assert (JProperties.class.getClassLoader().equals(new JProperties()
					.getClass().getClassLoader()));
			in = JProperties.class.getClassLoader().getResourceAsStream(name);
			assert (in != null);
			p.load(in);
			// return new
			// JProperties().getClass().getClassLoader().getResourceAsStream(name);
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
				properties = JProperties.loadProperties(OPEN_PROPERTIES,
						JProperties.BY_RESOURCEBUNDLE);
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
			properties = JProperties.loadProperties(propertiesFileName,
					JProperties.BY_RESOURCEBUNDLE);
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
			properties = JProperties.loadProperties(CONF_PROPERTIES,
					JProperties.BY_RESOURCEBUNDLE);
		} catch (IOException e) {
			log.error(CONF_PROPERTIES + ".properties not found");
		}
		if (properties.getProperty(key) == null) {
			return null;
		}
		return properties.getProperty(key).toString();
	}
	
	public static void main(String[] args){
		System.out.println("aaaaaaa==="+getMessage("agt_url"));
	}
	
}
