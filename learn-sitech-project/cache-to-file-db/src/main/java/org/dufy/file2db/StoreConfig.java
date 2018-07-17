/**
 * 
 */
package org.dufy.file2db;

/**
 * @author yinzl
 * 入库配置
 */
public class StoreConfig {

	public StoreConfig(){
		
	}
	
	//优先级
	private int priority;
	
	//类型
	private String type;
	
	//实现类名称
	private String className;

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}	
}
