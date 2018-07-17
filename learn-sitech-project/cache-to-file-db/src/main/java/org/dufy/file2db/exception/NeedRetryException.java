package org.dufy.file2db.exception;

import java.util.HashSet;
import java.util.Set;

/**
 * When insert failed, throw a retry exception for {@link org.dufy.file2db.StoreWorker} to
 * retry at next round.
 * 
 * @author Charlie
 *
 */
public class NeedRetryException extends Exception {

	private static final long serialVersionUID = -6583239134594269802L;
	
	public static Set<String> RETRY_KEY_SET = new HashSet<String>();
	
	static{
		//重试SQLException 信息包含关键key
		RETRY_KEY_SET.add("Connection reset");   	//连接失效
		RETRY_KEY_SET.add("Connection has been administratively disabled");   	//连接失效
		RETRY_KEY_SET.add("Socket read timed out");	//网络超时
		RETRY_KEY_SET.add("Already closed");   		//连接关闭
		RETRY_KEY_SET.add("关闭的连接"); 			//连接关闭
		RETRY_KEY_SET.add("Connection refused"); 	//连接拒绝
		RETRY_KEY_SET.add("Io 异常");				//IO异常
		RETRY_KEY_SET.add("The Network Adapter could not establish the connection");	//IO异常
		RETRY_KEY_SET.add("ORA-12170");				//连接超时		
		RETRY_KEY_SET.add("ORA-00054");				//资源忙		
	}
}
