package org.dufy.log.transfer;

/**
 * Factory class to get {@link FileTransfer} instance by protocol,
 * with calling {@link FileTransferFactory#getTransfer(String)}
 * 
 * @author Charlie
 *
 */
public class FileTransferFactory {
	
	private FileTransferFactory() {}
	
	/**
	 * Return a {@link FileTransfer} instance according to {@code protocol}
	 * 
	 * <p>Currently only support sftp
	 * 
	 * @param protocol file transfer protocol
	 * @return {@link FileTransfer} instance
	 */
	public static FileTransfer getTransfer(String protocol) {
		if("sftp".equalsIgnoreCase(protocol)) {
			return new SftpFileTransfer();
		}
		
		throw new IllegalArgumentException("Unsupported file transfer protocol!");
	}
}
