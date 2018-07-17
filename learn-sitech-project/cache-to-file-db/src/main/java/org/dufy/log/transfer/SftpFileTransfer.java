package org.dufy.log.transfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.io.FileUtils;
import org.dufy.log.transfer.FileTransferConfiguration.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;

import cn.hutool.core.util.StrUtil;

/**
 * A {@code FileTransfer} which use secure file transfer protocol.
 * The underlined SFTP library is {@code JSch}
 * 
 * @author Charlie
 * @see FileTransfer
 * @see JSch
 */
public class SftpFileTransfer implements FileTransfer {
	
	private Server server;
	private Session sshSession;
	private Channel channel;
	private final static Logger logger = LoggerFactory.getLogger("FileTransfer");
	private static ConcurrentMap<String, Integer> retryMap = new ConcurrentHashMap<String, Integer>(); 

	@Override
	public boolean connect(Server server) {
		this.server = server;
		if(isConnected() || server.getHost().equals("127.0.0.1")){ //本机传送
			return true;
		}
		JSch jsch = new JSch();
		try {
			this.sshSession = jsch.getSession(server.getUser(), server.getHost(), server.getPort());
			if(!StrUtil.isBlank(server.getKey())) {
			  if(!StrUtil.isBlank(server.getKeyPhrase())) {
			    jsch.addIdentity(server.getKey(), server.getKeyPhrase());
			  }
			  else {
			    jsch.addIdentity(server.getKey());
			  }
			}
			
			if(server.getPassword()!= null) {
				this.sshSession.setPassword(server.getPassword());
			}

			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			this.sshSession.setConfig(sshConfig);
			this.sshSession.connect();

			this.channel = this.sshSession.openChannel("sftp");
			this.channel.connect();
			ChannelSftp  sftp = (ChannelSftp) this.channel;				
			sftp.cd(server.getLocation());

			return true;
		} catch (Exception e) {
			disconnect();
			FileTransferExecutor.logger.error("connect to server failed:", e);
			return false;
		}
	}

	@Override
	public boolean isConnected() {
		boolean flag = false;
		if(this.channel != null && this.channel.isConnected()){
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean sendFile(File file, String targetFileName) {
		//if not connected,try to connect
		if(!isConnected() && !connect(this.server)) {
			return false;
		}
		
		if(server.getHost().equals("127.0.0.1")) {
			localRenameFile(file, server.getLocation(), targetFileName);
			return true;
		}
		
		ChannelSftp  sftp = (ChannelSftp) this.channel;	
		TransferProgressMonitor monitor = new TransferProgressMonitor();
		try {
			sftp.put(new FileInputStream(file), targetFileName+".tmp", monitor);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			FileTransferExecutor.logger.error("sftp传送时找不到本地文件！{}", file.getAbsolutePath(),e);
			return false;
		} catch (SftpException e) {
			e.printStackTrace();
			FileTransferExecutor.logger.error("sftp文件传送异常！{}", file.getAbsolutePath(),e);
			return false;
		}

		if(monitor.isEnd()){
			try {
				sftp.rename(targetFileName+".tmp", targetFileName);	
			} catch (SftpException e) {
				//e.printStackTrace();
				FileTransferExecutor.logger.error("sftp文件重命名异常！{}", file.getAbsolutePath(), e);
				Integer retryCount = retryMap.get(file.getAbsolutePath());
				if(retryCount == null){
					retryCount = 0;
				}
				if(retryCount <= 3){
					retryMap.put(file.getAbsolutePath(), retryCount + 1);
					return false;
				}else{
					FileTransferExecutor.logger.info("sftp文件{}重命名重试3次异常,终止重试,文件备份。", file.getAbsolutePath());
				}
			}
		}else{
			try {
				sftp.rename(targetFileName+".tmp", targetFileName+".err");
			}catch (SftpException e) {
				FileTransferExecutor.logger.error("sftp文件重命名异常！{}", file.getAbsolutePath(),e);
				return false;
			}
		}
		return true;
	}

	@Override
	public void disconnect() {
		if(this.channel != null){
			this.channel.disconnect();
		}
		this.channel = null;
		
		if(this.sshSession != null){
			this.sshSession.disconnect();
		}
		this.sshSession = null;
	}

	public class TransferProgressMonitor implements SftpProgressMonitor {

		private long transfered;
		private boolean isEnd;

		@Override
		public boolean count(long count) {
			transfered = transfered + count;
			FileTransferExecutor.logger.debug("Currently transferred total size: " + transfered + " bytes");
			return true;
		}

		@Override
		public void end() {
			FileTransferExecutor.logger.debug("Transferring done.");
			setEnd(true);
		}

		@Override
		public void init(int op, String src, String dest, long max) {
			FileTransferExecutor.logger.debug("Transferring begin.");
		}

		public boolean isEnd() {
			return isEnd;
		}

		public void setEnd(boolean isEnd) {
			this.isEnd = isEnd;
		}

	}
	
	/**
	 * rename file
	 * 
	 * @param file to be backup
	 */
	private void localRenameFile(File file, String targetPath, String targetName) {
		if(!file.exists()){ //本机传送或者文件已移无须备份
			return;
		}

		File targetDir = new File(targetPath);
		if(!targetDir.exists()){
			targetDir.mkdirs();
		}

		boolean moveFileResult=file.renameTo(new File(targetDir.getAbsolutePath()+File.separator+targetName));
		if(!moveFileResult){
			try{
				File destinationFile=new File(targetDir.getAbsolutePath()+File.separator+targetName);	
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
}
