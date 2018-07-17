package org.dufy.file2db;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public class StoreManager {

	public final static Logger logger = LoggerFactory.getLogger("file2db");
	
	private Map<String, Store> storeMap = StoreConfiguration.loadStores();;

	private StoreManager() {}
	
	public static StoreManager getInstance(){
		return StoreManager.instance;
	}
	
	@Resource
	StoreSignalObserver signalObserver;
	
	/**
	 * 开始入库
	 */
	public synchronized void start(){
		if(this.startFlag) {
			logger.info("The StoreManager has already started, do nothing...");
			return;
		}
		
		this.exitFlag=false;
		this.pauseFlag = false;
		this.exec.scheduleWithFixedDelay(new Runnable(){
			public void run() {
				processFile();
			}}, 10, 5, TimeUnit.SECONDS);
		this.startFlag = true;
		signalObserver.observe();
	}

	/**
	 * 扫描并处理数据文件
	 */
	private synchronized void processFile(){
		try{
			String path = StoreConfiguration.getInstance().getScanPath();
			File f = new File(path);
			if (!f.exists()) //创建目录
				f.mkdirs();	

			if(isExit()) {
				return;
			}

			List<FileInfo> files = getAllFile(path);
			if(files.size()==0){
				return;
			}
			
			dispatchAndProcessFile(files);	

			if(pauseFlag) {
				try {
					logger.info("收到暂停入库请求,因此将暂停入库5分钟,如有停应用操作请在看到提示后进行");
					System.out.println("收到暂停入库请求,因此将暂停入库5分钟,如有停应用操作请在看到提示后进行");
					Thread.sleep(120*1000);
					logger.info("入库线程已暂停2分钟,如果日志没有输出其它内容,可以停止应用");
					System.out.println("入库线程已暂停2分钟,如果日志没有输出其它内容,可以停止应用");
					Thread.sleep(180*1000); //暂停180s入库，方便上线重启
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				pauseFlag = false;
			}

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	private boolean exitFlag = false;
	public void exit(){
		this.exitFlag=true;
		this.exec.shutdown();
		this.pool.shutdown();
	}

	private boolean isExit() {
		return this.exitFlag;
	}

	private static class StoreRunnable implements Runnable, Comparable<StoreRunnable> {
		private final IStoreWorker sw;
		private final int priority;
		public StoreRunnable(IStoreWorker sw, int priority){
			this.sw = sw;
			this.priority = priority;
		}
		public int compareTo(StoreRunnable o) {
			return o.priority - this.priority;
		}
		public void run() {
			this.sw.start();
		}
	}

	/**
	 * 解析数据文件
	 * @param files
	 */
	private void dispatchAndProcessFile(List<FileInfo> files) {
		List<Future<?>> tList= new LinkedList<Future<?>>();
		for(int i=0;i<files.size();i++){
			FileInfo fi = files.get(i);
			Future<?> f ;
			Store store = storeMap.get(fi.type);
			if(store == null)
				continue;
			try {
				StoreWorker storeWorker = (StoreWorker)applicationContext.getBean(store.getStoreWorker());
				if(storeWorker == null) {
					storeWorker = (StoreWorker)Class.forName(store.getStoreWorker()).newInstance();
				}
				
				storeWorker.setFileAndType(fi.file, fi.type);
				f = this.pool.submit(new StoreRunnable(storeWorker, fi.code));
				tList.add(f);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		//执行完再继续处理文件
		try {
			while(!checkFutureList(tList)){
				this.pool.awaitTermination(1, TimeUnit.SECONDS); //等待1s				
				logger.info("pool.getQueue().size()==>"+pool.getQueue().size());
				logger.info("pool.getActiveCount()==>"+pool.getActiveCount());
			}
		} catch (Exception e) {
			//如果出现中断，服务停止退出,不用做任何操作
			logger.error("pool deal file catch exception:",e);
			return;
		}
	}

	//判断future list是否完成
	private boolean checkFutureList(List<Future<?>> tList){
		for(int i=0; i<tList.size();i++){
			Future<?> f  = tList.get(i);
			if(!f.isDone()){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 移走不可识别或未知的文件
	 * @param file
	 */
	private void moveUnknownFile(File file){
		if(file == null){
			return;
		}
		//error目录是允许存在的，所以跳过
		if(file.isDirectory() && file.getName().equals("error")){
			return;
		}
		//未知文件的丢弃放置目录
		String bakPath = StoreConfiguration.getInstance().getDiscardPath();
		File bakDir = new File(bakPath);
		if(!bakDir.exists()){
			bakDir.mkdirs();
		}		
		//移动到备份目录。为避免重名，加上时间戳后缀
		boolean successFlag = file.renameTo(new File(bakPath + File.separator+ file.getName() + "." + System.currentTimeMillis()));
		logger.info("扫描到不需入库的文件 " + file.getName() + ",移动到目录" + (successFlag ? "成功" : "失败"));
	}

	private static class FileInfo {
		File file;
		int code;
		String type;
	}

	private final static int MAX_PROCESS_THREAD = 100;
	private final static int CORE_POOL_SIZE	= 40;

	private Pattern scanPattern = Pattern.compile(StoreConfiguration.getInstance().getScanFilePattern());
	public Pattern getScanPattern() {
		return scanPattern;
	}

	private List<FileInfo> getAllFile(String path) {		
		File dir = new File(path);
		File[] files = dir.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				return pathname.isFile();
			}
		});
				
		Arrays.sort(files, new Comparator<File>(){ 
		    public int compare(File f1, File f2) 
		    { 		    	 
		        return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
		    } 
		}); 
		
		List<FileInfo> result = new ArrayList<FileInfo>();
		for(int i = 0; i < files.length; i++){
			logger.info("===file.getName======>>>>>>>>" + files[i].getName());
			Matcher match = this.scanPattern.matcher(files[i].getName());
			if(!match.matches()){
				moveUnknownFile(files[i]);
				continue;
			}
			int priority = 0;
			String prefix = match.group(1);
			logger.info("==prefix======>>>>>>>>" + prefix);
			Store store = storeMap.get(prefix);
			if(store == null){
				moveUnknownFile(files[i]);
				continue;
			}
			priority = store.getPriority();
			FileInfo fi = new FileInfo();		
			fi.file = files[i];
			fi.code = priority;
			fi.type = prefix;
			result.add(fi);
			System.out.println(new Date()+"  get list file name===>"+ files[i].getName()+" modifyTime===>"+files[i].lastModified());
		}

		files=null;
		return result;
	}

	private ThreadPoolExecutor pool = new ThreadPoolExecutor(CORE_POOL_SIZE,MAX_PROCESS_THREAD,0L,
			TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>()){
	  
	  protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
      return new PriorityFutureTask<T>(runnable, value);
	  }
	  
	  protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
      return new PriorityFutureTask<T>(callable);
	  }
	};
	private ScheduledThreadPoolExecutor exec=new ScheduledThreadPoolExecutor(1);
	static private StoreManager instance = new StoreManager();
	
	private boolean startFlag;
	private boolean pauseFlag;

	public void setPauseFlag(boolean pauseFlag) {
		this.pauseFlag = pauseFlag;
	}
	
	/**
	 * 停止扫描（供外部调用）
	 */
	public void stopScheduledThreadPoolExecutor(){
		this.exec.shutdown();
		this.startFlag = false;
	}
	
	@Resource
	private ApplicationContext applicationContext;
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	/**
	 * PriorityFutureTask for  can be put into a {@link PriorityBlockingQueue}
	 * @author Charlie
	 *
	 * @param <T>
	 */
	private class PriorityFutureTask<T> extends FutureTask<T> implements Comparable<PriorityFutureTask<T>> {
	  
	  private Runnable runnable;

    public PriorityFutureTask(Callable<T> callable) {
      super(callable);
    }
    
    public PriorityFutureTask(Runnable runnable, T result) {
      super(runnable, result);
      this.runnable = runnable;
    }
    
    public Runnable getRunnable() {
      return runnable;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compareTo(PriorityFutureTask<T> o) {
      if(runnable instanceof Comparable && o.getRunnable() instanceof Comparable) {
        try {
          return ((Comparable<Runnable>)runnable).compareTo(o.getRunnable());
        }
        catch(Throwable e) {
          return 0;
        }
      }
      return 0;
    }
	  
	}
}