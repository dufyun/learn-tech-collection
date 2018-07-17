package org.dufy.file2db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.zip.GZIPOutputStream;

import org.dufy.file2db.exception.NeedRetryException;
import org.dufy.log.DataLogger;
import org.dufy.util.GZFileReader;


public abstract class StoreWorker implements IStoreWorker{

	public void setFileAndType(File file,String type) {
		this.file = file;
		this.setFileType(type);		
	}
	private File file;//修改为变量
	private String fileType;
	
	private String recordEnd = String.valueOf(DataLogger.ASCII_RS);

	public void start() {
		try{
			processFile();	
		} catch (Throwable e){
			e.printStackTrace();
			StoreManager.logger.error("Store Data ERROR:" + e.getMessage(), e);
		}finally{
			//移除已处理数据
			String bakPath = StoreConfiguration.getInstance().getBackupPath();
			backupFile(this.file,bakPath);
			
			//移入重录数据
			mvRetryFile(getRetryFileName(this.file.getName()), getErrDir().getAbsolutePath(), StoreConfiguration.getInstance().getScanPath());
			System.out.println(new Date()+"  deal file name ====>"+this.file.getName()+" modifyTime===>"+this.file.lastModified());
		}
	}

	/**
	 * 备份数据文件
	 * @param file
	 * @param path
	 */
	private void backupFile(File file,String path) {
		Matcher m  = StoreManager.getInstance().getScanPattern().matcher(file.getName());
		if(!m.matches())
			return;
		
		File yDir = new File(path+File.separator+m.group(2)+m.group(3)+m.group(4));
		if(!yDir.exists()){
			yDir.mkdirs();
		}
		file.renameTo(new File(yDir.getAbsolutePath()+File.separator+file.getName()));
	}
	
	/**
	 * 处理文件
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void processFile() throws FileNotFoundException, IOException{
		//路径检查
		File dir = getErrDir();

		//错误文件
		File error = new File(dir.getAbsoluteFile()+File.separator+this.file.getName()+".error");
		//重录文件		
		File retry = new File(dir.getAbsoluteFile()+File.separator+getRetryFileName(this.file.getName()));
		FileOutputStream errOut = null;
		GZIPOutputStream  gzout = null; 
		
		GZFileReader fr = new GZFileReader(this.file);
		String str = null;
		StringBuilder buf = new StringBuilder();
		while((str = fr.readLine())!=null){
			buf.append(str);
			if(!str.endsWith(recordEnd)) {
				continue;
			}
			else {
				//Remove RS
				buf.deleteCharAt(buf.length()-1);
			}
			
			boolean tmp = true;
			try{
				//对于一行只有换行符的情况，跳过
				if(buf.length()<4){
					continue;
				}
				tmp = processRecorder(buf.toString());
			} catch (NeedRetryException e){ //写重录文件
				if(gzout == null)
					gzout = new GZIPOutputStream(new FileOutputStream(retry));
				gzout.write((buf.toString()+DataLogger.ASCII_RS+"\n").getBytes());
			}
			catch (Throwable e){
				tmp = false;
				StoreManager.logger.error("processRecorder Exception! recorder:"+buf.toString(),e);
			} 
			if(!tmp){ //入库失败
				if(errOut == null)
					errOut = new FileOutputStream(error);
				errOut.write((buf.toString()+DataLogger.ASCII_RS+"\n").getBytes());
			}
			
			buf.delete(0, buf.length());
		}
		if(errOut != null)
			errOut.close();
		if(gzout != null){
			gzout.finish();
			gzout.close(); 
		}
		fr.close();
	}
	
	private File getErrDir(){
		//路径检查
		File dir = new File(this.file.getParentFile().getAbsolutePath()+File.separator+"error");
		if (dir.exists()){
			if(!dir.isDirectory()) {
				dir.delete();
				dir.mkdirs();
			}
		} else {
			dir.mkdirs();
		}
		return dir;
	}
	
	//获取扫描文件重录次数
	private int getRetryCount(String filename){
		char ch = filename.charAt(filename.length()-1);
		int count =0;
		if(Character.isDigit(ch))
			count = Character.getNumericValue(ch);
		return count;
	}
	
	//获取重录扫描文件
	private String getRetryFileName(String filename){
		int count = getRetryCount(filename);
		if(count==0){
			return filename.concat("1");
		}else{
			String rc = String.valueOf(count+1);
			return filename.substring(0, filename.length()-1).concat(rc);
		}
	}
	
	//将重录文件移到扫描路径
	private  boolean  mvRetryFile(String retryFileName,String retryDir,String scanDir){
		File retryFile = new File(retryDir+File.separator+retryFileName);
		try{			
			if(retryFile.length() == 0 ){ //空文件删除
				retryFile.delete();
				return true;
			}else if(getRetryCount(retryFileName) > StoreConfiguration.getInstance().getRetryTimes()){ //超出重录次数删除
				retryFile.delete();
				return false;
			}else{
				return retryFile.renameTo(new File(scanDir+File.separator+retryFileName));
			}			
		}catch (Exception e){
			StoreManager.logger.error("mvRetryFile exception:" + e.getMessage(), e);
			return false;
		}
	}
	
	//判断是否重录Exception
	protected boolean isRetryException(String msg) {
		for (Iterator<String> iterator = NeedRetryException.RETRY_KEY_SET.iterator(); iterator.hasNext();) {
			String retryKey = iterator.next();
			if (msg.contains(retryKey)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * 将str数据写入conn连接的数据库。
	 */
	protected abstract boolean processRecorder(String str) throws Throwable;

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
}
