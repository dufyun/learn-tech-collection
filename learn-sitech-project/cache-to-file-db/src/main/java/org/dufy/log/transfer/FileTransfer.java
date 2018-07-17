package org.dufy.log.transfer;

import java.io.File;

import org.dufy.log.transfer.FileTransferConfiguration.Server;

/**
 * 
 * 
 * @author Charlie
 *
 */
public interface FileTransfer {

	/**
	 * connect to the file server
	 * 
	 * @param server where files are transfered to
	 * @return
	 */
	boolean connect(Server server);
	
	boolean isConnected();
	
	boolean sendFile(File srcFile, String targetFileName);
	
	void disconnect();
}
