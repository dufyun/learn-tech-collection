package org.dufy.log.logback;

import java.io.File;
import java.util.Date;

import ch.qos.logback.core.rolling.RollingPolicy;
import ch.qos.logback.core.rolling.TriggeringPolicy;
import ch.qos.logback.core.rolling.helper.ArchiveRemover;
import ch.qos.logback.core.rolling.helper.DateTokenConverter;
import ch.qos.logback.core.rolling.helper.TimeBasedArchiveRemover;
import ch.qos.logback.core.spi.ContextAwareBase;

/**
 * An enhanced version of {@code DefaultTimeBasedFileNamingAndTriggeringPolicy} 
 * using a custom {@link DataLogRollingPolicy} as its default {@link RollingPolicy}.
 * 
 * <p>The trigger won't be fired with {@code null} message event only if the log file is not empty, 
 * to avoid too much empty files being created.
 * 
 * @author Ceki
 * @author Charlie
 * @param <E>
 * @see ch.qos.logback.core.rolling.DefaultTimeBasedFileNamingAndTriggeringPolicy
 */
public class DataLogTriggeringPolicy<E> extends ContextAwareBase implements TriggeringPolicy<E> {
	
	protected DataLogRollingPolicy<E> rollingPolicy;
	protected ArchiveRemover archiveRemover = null;
	protected String elapsedPeriodsFileName;
	protected DataLogRollingCalendar rc;

	protected long artificialCurrentTime = -1;
	protected Date dateInCurrentPeriod = null;
	//Indicate whether the current log file is empty
	volatile boolean logFileIsEmpty = false;

	protected long nextCheck;
	protected boolean started = false;

	@Override
	public void start() {
		@SuppressWarnings("rawtypes")
		DateTokenConverter dtc = rollingPolicy.fileNamePattern.getPrimaryDateTokenConverter();
	    if (dtc == null) {
	      throw new IllegalStateException("FileNamePattern ["
	              + rollingPolicy.fileNamePattern.getPattern()
	              + "] does not contain a valid DateToken");
	    }

	    rc = new DataLogRollingCalendar();
	    rc.init(dtc.getDatePattern(), rollingPolicy.getRollingInterval());
	    addInfo("The date pattern is '" + dtc.getDatePattern()
	            + "' from file name pattern '" + rollingPolicy.fileNamePattern.getPattern()
	            + "'.");
	    rc.printPeriodicity(this);

	    setDateInCurrentPeriod(new Date(getCurrentTime()));
	    if (rollingPolicy.getParentsRawFileProperty() != null) {
	      File currentFile = new File(rollingPolicy.getParentsRawFileProperty());
	      if (currentFile.exists() && currentFile.canRead()) {
	        setDateInCurrentPeriod(new Date(currentFile.lastModified()));
	      }
	    }

	    addInfo("Setting initial period to " + dateInCurrentPeriod);
	    computeNextCheck();
	    
	    archiveRemover = new TimeBasedArchiveRemover(rollingPolicy.fileNamePattern, rc);
	    archiveRemover.setContext(context);
	    started = true;
	}

	@Override
	public void stop() {
		 started = false;
	}
	
	protected void computeNextCheck() {
		nextCheck = rc.getNextTriggeringMillis(dateInCurrentPeriod);
	}

	protected void setDateInCurrentPeriod(long now) {
		dateInCurrentPeriod.setTime(now);
	}
	
	// allow Test classes to act on the dateInCurrentPeriod field to simulate old
	// log files needing rollover
	public void setDateInCurrentPeriod(Date _dateInCurrentPeriod) {
		this.dateInCurrentPeriod = _dateInCurrentPeriod;
	}
	
	@Override
	public boolean isStarted() {
		return started;
	}

	@Override
	public boolean isTriggeringEvent(File activeFile, E event) {
		long time = getCurrentTime();
		
		if (time >= nextCheck && !logFileIsEmpty) {
			Date dateOfElapsedPeriod = dateInCurrentPeriod;
			addInfo("Elapsed period: " + dateOfElapsedPeriod);
			elapsedPeriodsFileName = rollingPolicy.fileNamePatternWCS.convert(dateOfElapsedPeriod);
			setDateInCurrentPeriod(time);
			computeNextCheck();
			return true;
		} else {
			return false;
		}
	}

	public void setRollingPolicy(DataLogRollingPolicy<E> rollingPolicy) {
		this.rollingPolicy = rollingPolicy;
	}

	/**
	 * Return the file name for the elapsed periods file name.
	 * 
	 * @return
	 */
	String getElapsedPeriodsFileName() {
		return elapsedPeriodsFileName;
	}

	/**
	 * Return the current periods file name without the compression suffix. This
	 * value is equivalent to the active file name.
	 * 
	 * @return current period's file name (without compression suffix)
	 */
	String getCurrentPeriodsFileNameWithoutCompressionSuffix() {
		return rollingPolicy.fileNamePatternWCS.convert(dateInCurrentPeriod);
	}

	  /**
	   * Return the archive remover appropriate for this instance.
	   */
	  ArchiveRemover getArchiveRemover() {
		return null;
	}
	  
	/**
	 * Return the current time which is usually the value returned by
	 * System.currentMillis(). However, for <b>testing</b> purposed this value
	 * may be different than the real time.
	 * 
	 * @return current time value
	 */
	long getCurrentTime() {
		// if time is forced return the time set by user
		if (artificialCurrentTime >= 0) {
			return artificialCurrentTime;
		} else {
			return System.currentTimeMillis();
		}
	}

	/**
	 * Set the current time. Only unit tests should invoke this method.
	 * 
	 * @param timeInMillis
	 */
	void setCurrentTime(long timeInMillis) {
		artificialCurrentTime = timeInMillis;
	}

	/**
	 * Set some date in the current period. Only unit tests should invoke this
	 * method.
	 * 
	 * WARNING: method removed. A unit test should not set the date in current
	 * period. It is the job of the FNATP to compute that.
	 * 
	 * @param date
	 */
	// void setDateInCurrentPeriod(Date date);
}
