package org.dufy.log.transfer;


import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.FileUtils;
import org.dufy.log.transfer.FileTransferConfiguration.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Executor of file transfer task
 * 
 * @author Charlie
 *
 */
public class FileTransferExecutor {
	
	public static Logger logger = LoggerFactory.getLogger("fileTransfer");

	private ScheduledThreadPoolExecutor threadExecutor = new ScheduledThreadPoolExecutor(1);
	
	private Parameter fileTransferParameter;
	
	private FileTransfer transfer;

	public FileTransferExecutor(Parameter fileTransferParameter) {
		this.fileTransferParameter = fileTransferParameter;
		this.transfer = FileTransferFactory.getTransfer(fileTransferParameter.getTransferServer().getProtocol());
	}
	
	/**
	 * Start transfer files
	 */
	public void execute() {
		threadExecutor.scheduleWithFixedDelay(new Runnable(){
				public void run() { 
					try {
						processFile();
					}
					catch(Throwable e) {
						e.printStackTrace();
						logger.error("FileTransfer processFile catch a error:", e);
					}
				}
			}, 
			5,
			fileTransferParameter.getInterval(),
			TimeUnit.SECONDS
		);
	}
	
	/**
	 * file search, compress, transfer and backup process
	 */
	private synchronized void processFile() {
		
		//try to roll the data files
		for(String loggerName : fileTransferParameter.getFileType().split(",")) {
			
			//A empty message won't write to file when the logger pattern is "%message",
			//but it can fire the logger rolling trigger if time elapsed enough
			LoggerFactory.getLogger(loggerName).error("");
		}

		//scan and get matched files
		File dir = new File(fileTransferParameter.getScanPath());
		File[] files = dir.listFiles();
		for(File file : files) {
			if(!file.isFile()) {
				continue;
			}
			
			Matcher matt = fileTransferParameter.getCompiledFilePattern().matcher(file.getName());
			if(!matt.matches()) {
				continue;
			}
			
			//discard the empty files
			if(file.length() == 0){
				file.delete();
				continue;
			}
			
			//connect to the server after confirm there're files to be processed in order to save resources
			if(!transfer.isConnected()) {
			  if(!transfer.connect(fileTransferParameter.getTransferServer())){
  	      logger.error("Connect to {} failed, cannot transfer data files!", fileTransferParameter.getTransferServer().getHost());
  	      return;
			  }
	    }
			
			File zipFile = null;
			File sourceFile = file;
			String sourFilePath = sourceFile.getAbsolutePath();
			
			//compress files
			if("gz".equals(fileTransferParameter.getCompressType()) && !sourFilePath.endsWith(".gz")){
				File proFile = null;
				String zipPath = null;
				if(!sourFilePath.endsWith(".pro")){			
					zipPath = sourFilePath + ".gz";
					String proFilePath = sourceFile.getAbsolutePath() + ".pro";
					if(sourceFile.renameTo(new File(proFilePath))){
						proFile = new File(proFilePath);
					}else{
						logger.error("使用 File.renameTo 重命名处理中文件失败。" + sourceFile.getAbsolutePath());
						proFile = sourceFile;
					}
				}else{
					proFile = sourceFile;
					zipPath = sourFilePath.substring(0, sourFilePath.length() - 4) + ".gz";
				}
				zipFile = new File(zipPath); 
				try {
					compressFile(proFile, zipFile);
					proFile.delete();
				} catch (IOException e) {
					zipFile.delete();
					logger.error("压缩本地文件失败" + proFile.getAbsolutePath(), e);
					continue;
				}
			}else{
				zipFile = sourceFile;
			}

			//transfer files (compressed if configured)
			if(transfer.sendFile(zipFile, matt.replaceAll(fileTransferParameter.getDestFileRenamePattern()))){
				
				//backup files
				backupFile(zipFile, matt.replaceAll(fileTransferParameter.getBackupPath()));
			}

		}
		
		//try to disconnect the connection
		if(transfer.isConnected()){
			transfer.disconnect();
		}
	}
	
	/**
	 * Get all files match {@code pattern} in directory {@code path}, and discards empty files.
	 * 
	 * @param path the directory to scan
	 * @param pattern the filename pattern to match wanted files
	 * @return
	 */
	protected File[] getFiles(String path, final Pattern pattern) {
		File dir = new File(path);

		File[] files = dir.listFiles(new FileFilter() {
			public boolean accept(File file) {
				Matcher matt = pattern.matcher(file.getName());
				if (!matt.matches())
					return false;
				
				//discard the empty files
				if (file.length() == 0){
					file.delete();
					return false;
				}
				return true;
			}
		});
		return files;
	}
	
	/**
	 * 
	 * @param file
	 * @param outFile
	 * @throws IOException
	 */
	private void compressFile(File file, File outFile) throws IOException {		

		FileInputStream in = new FileInputStream(file);
        GZIPOutputStream out = null;
        try {
	        out = new GZIPOutputStream(new FileOutputStream(outFile));
	        byte[] buf = new byte[10240];  
	        int len = 0;       
	        while (((in.available()>10240)&& (in.read(buf)) > 0)) {    
	        	out.write(buf);               
	        } 

	        len = in.available();              
	        in.read(buf, 0, len);
	        out.write(buf, 0, len);
	        out.flush();
        } finally{
        	in.close();
        	out.close();
        }
 	}
	

	
	/**
	 * move the file to backup directory
	 * 
	 * @param file to be backup
	 */
	private void backupFile(File file, String backupPath) {
		if(!file.exists()){ //本机传送或者文件已移无须备份
			return;
		}

		File backupDir = new File(backupPath);
		if(!backupDir.exists()){
			backupDir.mkdirs();
		}

		boolean moveFileResult=file.renameTo(new File(backupDir.getAbsolutePath()+File.separator+file.getName()));
		if(!moveFileResult){
			try{
				File destinationFile=new File(backupDir.getAbsolutePath()+File.separator+file.getName());	
				FileUtils.copyFile(file, destinationFile);	
				if(!FileUtils.deleteQuietly(file)){
					for(int i = 0;i <= 10;i++){
						Thread.sleep(6000);
						if(FileUtils.deleteQuietly(file)){
							break;
						}
					}
				}
				logger.info("使用 org.apache.commons.io.FileUtils.copyFile 备份文件"+file.getAbsolutePath()+"成功。");
			}
			catch(Exception e){
				logger.error("使用 org.apache.commons.io.FileUtils.copyFile 备份文件"+ file.getAbsolutePath() +"出现异常。",e);
			}			
		}
		else{
			logger.info("使用 File.renameTo 备份文件"+ file.getAbsolutePath() +"成功。");
		}
	}

	public void shutdown() {
		threadExecutor.shutdown();
	}
}
