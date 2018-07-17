package org.dufy.file2db;

/**
 * {@code Store} represents a type of data file import task.
 * Each Store should have a associated {@link StoreWorker} to process the files.
 * The {@code priority} of which decides the proceeding order when multiple types of
 * {@code StoreWorker} are working.
 * 
 * @author Charlie
 * @see StoreWorker
 *
 */
public class Store {
	
	//优先级，值越大优先级高
	private int priority;
	
	//类型
	private String storeType;

	//实现类名称
	private String storeWorker;


	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public String getStoreWorker() {
		return storeWorker;
	}

	public void setStoreWorker(String storeWorkerClass) {
		this.storeWorker = storeWorkerClass;
	}
	
	
}
