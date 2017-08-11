package com.learn.redisson.manager;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * Redission util!
 * 使用哨兵模式进行配置
 */
public class RedissonManager {

	private static RedissonClient redissonClient;
	private static Config config = new Config();

	/**
	 * 初始化Redisson，使用哨兵模式
	 */
	public static void init(){
		try {
			config.useSentinelServers()
					.setMasterName("mymaster")
					.addSentinelAddress("127.0.0.1:26379","127.0.0.1:26479", "127.0.0.1:26579")
					//同任何节点建立连接时的等待超时。时间单位是毫秒。默认：10000
					.setConnectTimeout(30000)
					//当与某个节点的连接断开时，等待与其重新建立连接的时间间隔。时间单位是毫秒。默认:3000
					.setReconnectionTimeout(10000)
					//等待节点回复命令的时间。该时间从命令发送成功时开始计时。默认:3000
					.setTimeout(10000)
					//如果尝试达到 retryAttempts（命令失败重试次数） 仍然不能将命令发送至某个指定的节点时，将抛出错误。如果尝试在此限制之内发送成功，则开始启用 timeout（命令等待超时） 计时。默认值：3
					.setRetryAttempts(5)
					//在一条命令发送失败以后，等待重试发送的时间间隔。时间单位是毫秒。     默认值：1500
					.setRetryInterval(3000)
			;
			redissonClient = Redisson.create(config);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 获取Redisson的实例对象
	 * @return
	 */
	public static Redisson getRedisson(){
		init();
		return (Redisson) redissonClient;
	}

	/**
	 * 测试哨兵模式的Redisson是否正常
	 * @param args
	 */
	public static void main(String[] args) {
		Redisson redisson = RedissonManager.getRedisson();
		System.out.println("redisson = " + redisson);

	}


}
