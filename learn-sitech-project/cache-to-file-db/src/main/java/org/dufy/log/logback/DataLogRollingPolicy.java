package org.dufy.log.logback;

import java.io.File;
import java.util.Date;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.rolling.RollingPolicyBase;
import ch.qos.logback.core.rolling.RolloverFailure;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.rolling.TriggeringPolicy;
import ch.qos.logback.core.rolling.helper.ArchiveRemover;
import ch.qos.logback.core.rolling.helper.AsynchronousCompressor;
import ch.qos.logback.core.rolling.helper.CompressionMode;
import ch.qos.logback.core.rolling.helper.Compressor;
import ch.qos.logback.core.rolling.helper.FileFilterUtil;
import ch.qos.logback.core.rolling.helper.FileNamePattern;
import ch.qos.logback.core.rolling.helper.RenameUtil;

/**
 * A {@code RollingPolicy} similar with {@linkplain TimeBasedRollingPolicy}, but with capability of rolling with custom interval.
 * 
 * <p>When no {@code <rollingInterval>} node exists in configuration, the rolling interval is 1.
 * 
 * @author Ceki G&uuml;lc&uuml;
 * @author Charlie
 * @param <E>
 * 
 * @see ch.qos.logback.core.rolling.TimeBasedRollingPolicy
 */
public class DataLogRollingPolicy<E> extends RollingPolicyBase implements TriggeringPolicy<E> {

	static final String FNP_NOT_SET = "The FileNamePattern option must be set before using TimeBasedRollingPolicy. ";
	static final int INFINITE_HISTORY = 0;
	static final int DEFAULT_ROLLING_INTERVAL = 1;

	FileNamePattern fileNamePattern;
	private int rollingInterval = DEFAULT_ROLLING_INTERVAL;
	
	// WCS: without compression suffix
	FileNamePattern fileNamePatternWCS;
	
	// use to name files within zip file, i.e. the zipEntry
	FileNamePattern zipEntryFileNamePattern;

	private Compressor compressor;
	private RenameUtil renameUtil = new RenameUtil();
	Future<?> future;

	private int maxHistory = INFINITE_HISTORY;
	private ArchiveRemover archiveRemover;

	DataLogTriggeringPolicy<E> triggeringPolicy;

	boolean cleanHistoryOnStart = false;

	@Override
	public void start() {
		// set the LR for our utility object
		renameUtil.setContext(this.context);

		// find out period from the filename pattern
		if (fileNamePatternStr != null) {
			fileNamePattern = new FileNamePattern(fileNamePatternStr, this.context);
			determineCompressionMode();
		} else {
			addWarn(FNP_NOT_SET);
			addWarn(CoreConstants.SEE_FNP_NOT_SET);
			throw new IllegalStateException(FNP_NOT_SET + CoreConstants.SEE_FNP_NOT_SET);
		}

		compressor = new Compressor(compressionMode);
		compressor.setContext(context);

		// wcs : without compression suffix
		fileNamePatternWCS = new FileNamePattern(Compressor.computeFileNameStr_WCS(fileNamePatternStr, compressionMode),
				this.context);

		addInfo("Will use the pattern " + fileNamePatternWCS + " for the active file");

		if (compressionMode == CompressionMode.ZIP) {
			String zipEntryFileNamePatternStr = transformFileNamePattern2ZipEntry(fileNamePatternStr);
			zipEntryFileNamePattern = new FileNamePattern(zipEntryFileNamePatternStr, context);
		}

		if (triggeringPolicy == null) {
			triggeringPolicy = new DataLogTriggeringPolicy<E>();
		}
		triggeringPolicy.setContext(context);
		triggeringPolicy.setRollingPolicy(this);
		triggeringPolicy.start();

		// the maxHistory property is given to TimeBasedRollingPolicy instead of
		// to the TimeBasedFileNamingAndTriggeringPolicy. This makes it more
		// convenient
		// for the user at the cost of inconsistency here.
		if (maxHistory != INFINITE_HISTORY) {
			archiveRemover = triggeringPolicy.getArchiveRemover();
			archiveRemover.setMaxHistory(maxHistory);
			if (cleanHistoryOnStart) {
				addInfo("Cleaning on start up");
				archiveRemover.clean(new Date(triggeringPolicy.getCurrentTime()));
			}
		}

		super.start();
	}

	@Override
	public void stop() {
		if (!isStarted()){
			return;
		}
		waitForAsynchronousJobToStop();
		super.stop();
	}

	private void waitForAsynchronousJobToStop() {
		if (future != null) {
			try {
				future.get(CoreConstants.SECONDS_TO_WAIT_FOR_COMPRESSION_JOBS, TimeUnit.SECONDS);
			} catch (TimeoutException e) {
				addError("Timeout while waiting for compression job to finish", e);
			} catch (Exception e) {
				addError("Unexpected exception while waiting for compression job to finish", e);
			}
		}
	}

	private String transformFileNamePattern2ZipEntry(String fileNamePatternStr) {
		String slashified = FileFilterUtil.slashify(fileNamePatternStr);
		return FileFilterUtil.afterLastSlash(slashified);
	}

	public DataLogTriggeringPolicy<E> getTriggeringPolicy() {
		return triggeringPolicy;
	}

	public void setTriggeringPolicy(DataLogTriggeringPolicy<E> triggeringPolicy) {
		this.triggeringPolicy = triggeringPolicy;
	}


	@Override
	public void rollover() throws RolloverFailure {

		// when rollover is called the elapsed period's file has
		// been already closed. This is a working assumption of this method.

		String elapsedPeriodsFileName = triggeringPolicy.getElapsedPeriodsFileName();

		String elapsedPeriodStem = FileFilterUtil.afterLastSlash(elapsedPeriodsFileName);
		
		if(new File(getParentsRawFileProperty()).length() == 0) {
		  return;
		}

		if (compressionMode == CompressionMode.NONE) {
			if (getParentsRawFileProperty() != null) {
				renameUtil.rename(getParentsRawFileProperty(), elapsedPeriodsFileName);
			} // else { nothing to do if CompressionMode == NONE and
				// parentsRawFileProperty == null }
		} else {
			if (getParentsRawFileProperty() == null) {
				future = asyncCompress(elapsedPeriodsFileName, elapsedPeriodsFileName, elapsedPeriodStem);
			} else {
				future = renamedRawAndAsyncCompress(elapsedPeriodsFileName, elapsedPeriodStem);
			}
		}

		if (archiveRemover != null) {
			archiveRemover.clean(new Date(triggeringPolicy.getCurrentTime()));
		}
	}

	Future<?> asyncCompress(String nameOfFile2Compress, String nameOfCompressedFile, String innerEntryName)
			throws RolloverFailure {
		AsynchronousCompressor ac = new AsynchronousCompressor(compressor);
		return ac.compressAsynchronously(nameOfFile2Compress, nameOfCompressedFile, innerEntryName);
	}

	Future<?> renamedRawAndAsyncCompress(String nameOfCompressedFile, String innerEntryName) throws RolloverFailure {
		String parentsRawFile = getParentsRawFileProperty();
		String tmpTarget = parentsRawFile + System.nanoTime() + ".tmp";
		renameUtil.rename(parentsRawFile, tmpTarget);
		return asyncCompress(tmpTarget, nameOfCompressedFile, innerEntryName);
	}

	/**
	 * 
	 * The active log file is determined by the value of the parent's filename
	 * option. However, in case the file name is left blank, then, the active
	 * log file equals the file name for the current period as computed by the
	 * <b>FileNamePattern</b> option.
	 * 
	 * <p>
	 * The RollingPolicy must know whether it is responsible for changing the
	 * name of the active file or not. If the active file name is set by the
	 * user via the configuration file, then the RollingPolicy must let it like
	 * it is. If the user does not specify an active file name, then the
	 * RollingPolicy generates one.
	 * 
	 * <p>
	 * To be sure that the file name used by the parent class has been generated
	 * by the RollingPolicy and not specified by the user, we keep track of the
	 * last generated name object and compare its reference to the parent file
	 * name. If they match, then the RollingPolicy knows it's responsible for
	 * the change of the file name.
	 * 
	 */
	@Override
	public String getActiveFileName() {
		String parentsRawFileProperty = getParentsRawFileProperty();
		if (parentsRawFileProperty != null) {
			return parentsRawFileProperty;
		} else {
			return triggeringPolicy.getCurrentPeriodsFileNameWithoutCompressionSuffix();
		}
	}
	@Override
	public boolean isTriggeringEvent(File activeFile, final E event) {
		return triggeringPolicy.isTriggeringEvent(activeFile, event);
	}

	/**
	 * Get the number of archive files to keep.
	 * 
	 * @return number of archive files to keep
	 */
	public int getMaxHistory() {
		return maxHistory;
	}

	/**
	 * Set the maximum number of archive files to keep.
	 * 
	 * @param maxHistory
	 *            number of archive files to keep
	 */
	public void setMaxHistory(int maxHistory) {
		this.maxHistory = maxHistory;
	}

	public boolean isCleanHistoryOnStart() {
		return cleanHistoryOnStart;
	}

	/**
	 * Should archive removal be attempted on application start up? Default is
	 * false.
	 * 
	 * @since 1.0.1
	 * @param cleanHistoryOnStart
	 */
	public void setCleanHistoryOnStart(boolean cleanHistoryOnStart) {
		this.cleanHistoryOnStart = cleanHistoryOnStart;
	}

	public int getRollingInterval() {
		return rollingInterval;
	}

	public void setRollingInterval(int rollingInterval) {
		this.rollingInterval = rollingInterval;
	}

}