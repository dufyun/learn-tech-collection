package org.dufy.file2db;

import javax.annotation.Resource;

import org.dufy.annotation.Module;
import org.dufy.file2db.common.IModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;


/**
 * A livechat back-end module used import data logs to database.
 * 
 * <p>It will automatically start/stop after {@link ApplicationContext} 
 * initialized/destruction.
 * 
 * @author Charlie
 *
 */
@Module
public class File2DBModuleController implements IModule {
  
  private final static Logger logger = LoggerFactory.getLogger(File2DBModuleController.class);
	
	@Resource
	private StoreManager storemanager;

	/* 启动入库模块
	 */
	public void startup() {
	  logger.info("File2DBModuleController now start import files to database ...");
		storemanager.start();
	}

	/* 关闭入库模块
	 */
	public void shutdown() {
		System.out.println("开始释放入库模块的资源... ...");
		try{
			StoreManager.getInstance().stopScheduledThreadPoolExecutor();
			System.out.println("入库模块资源释放完毕");
		}
		catch(Exception e){
			System.out.println("释放入库模块的资源时出现异常!");
			e.printStackTrace();
		}
	}

	@Bean(name="storeManager")
	public StoreManager getStoremanager() {
		return StoreManager.getInstance();
	}

	public void setStoremanager(StoreManager storemanager) {
		this.storemanager = storemanager;
	}

}
