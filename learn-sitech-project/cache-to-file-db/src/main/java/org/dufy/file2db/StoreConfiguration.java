package org.dufy.file2db;

import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

/**
 * Store Configuration holder and manager class
 * 
 * <p>Though we don't set the construction function as private for yaml to work correctly,
 * you should use {@link #getInstance()} to get a common instance for efficiency.
 * 
 * @author Charlie
 *
 */
public class StoreConfiguration {
	
	private String scanPath;
	private String scanFilePattern;
	private String backupPath;
	private String discardPath;
	private int retryTimes;
	
	private Map<String, Store> stores;
	
	public final static String CONFIG_FILE_NAME_TEST = "file2dbTest.yaml";
	public final static String CONFIG_FILE_NAME = "file2db.yaml";

	private static StoreConfiguration instance;
	private static byte[] LOCK = {};
	
	/**
	 * Though we don't set the construction function as private,
	 * you should use this 
	 * 
	 * @return
	 */
	public static StoreConfiguration getInstance() {
		if(instance == null) {
			synchronized(LOCK) {
				if(instance == null) instance = parseConfiguration();
			}
		}
		return instance;
	}

	private static StoreConfiguration parseConfiguration() {
		
		InputStream is = null;
		is = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE_NAME_TEST);
		
		if(is == null) {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
		}
		
		if(is == null) {
			throw new RuntimeException("File2db config file is not found!");
		}
		
		Yaml yaml = new Yaml();
		return (StoreConfiguration)yaml.load(is);
	}

	public static Map<String, Store> loadStores(){
		return getInstance().getStores();
	}

	public String getScanPath() {
		return scanPath;
	}

	public void setScanPath(String scanPath) {
		this.scanPath = scanPath;
	}

	public String getScanFilePattern() {
		return scanFilePattern;
	}

	public void setScanFilePattern(String scanFilePattern) {
		this.scanFilePattern = scanFilePattern;
	}

	public String getBackupPath() {
		return backupPath;
	}

	public void setBackupPath(String backupPath) {
		this.backupPath = backupPath;
	}

	public int getRetryTimes() {
		return retryTimes;
	}

	public void setRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
	}

	public Map<String, Store> getStores() {
		return stores;
	}

	public void setStores(Map<String, Store> stores) {
		this.stores = stores;
	}

	public String getDiscardPath() {
		return discardPath;
	}

	public void setDiscardPath(String discardPath) {
		this.discardPath = discardPath;
	}

	
}
