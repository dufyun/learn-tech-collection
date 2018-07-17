package org.dufy.log.transfer;

import java.util.LinkedList;
import java.util.List;

import org.dufy.log.transfer.FileTransferConfiguration.Parameter;
import org.dufy.util.JProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Manager of file transfer tasks.
 * 
 * @author Charlie
 *
 */
public class FileTransferManager {
  
  public final static Logger logger = LoggerFactory.getLogger("fileTransfer");
	
	private FileTransferManager () {}
	private static FileTransferManager instance = new FileTransferManager();
	
	private boolean initialized = false;
	public List<FileTransferConfiguration.Parameter> transferTasks;
	public List<FileTransferExecutor> executors;
	
	public static FileTransferManager getInstance() {
		return instance;
	}
	
	/**
	 * Initialize file transfer tasks
	 */
	public synchronized void initialize() {
		if(!initialized) {
			transferTasks = FileTransferConfiguration.loadTransferTasks();
			executors = new LinkedList<FileTransferExecutor>();
			initialized = true;
		}
	}

	public void start() {
	  logger.info("FileTransferManager now starts ...");
		if(!initialized){
			initialize();
		}
		
		for(Parameter transferTask : transferTasks) {
			FileTransferExecutor executor = new FileTransferExecutor(transferTask);
			transferTask.setDestFileRenamePattern(JProperties.getMessage("machine", "destFileRenamePattern"));
			logger.info("TransferTask {} is scheduled ...", transferTask.getFileType());
			executor.execute();
			executors.add(executor);
		}
	}

	public void stop() {
		for(FileTransferExecutor e : executors) {
			e.shutdown();
		}
		logger.info("All transferTasks are stoped ...");
	}
	
}
