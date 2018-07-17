package org.dufy.log.transfer;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * A {@link ApplicationListener} to start file transfer process
 * when context initialized, and stop when context destroyed.
 * 
 * @author dufy
 * spring 容器启动完成执行下面方法
 *
 */
@Component
public class FileTransferStartListener implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			FileTransferManager.getInstance().start();
		}
		catch(Throwable e) {
			e.printStackTrace();
			FileTransferManager.logger.error("FileTransferStartListener start failed!", e);
		}
	}
}
