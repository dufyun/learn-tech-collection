package org.learn.rest.aop;

/**
 * The {@code AopLoggingHelper} is used to store parameters and flags 
 * of current logging.
 * 
 * @author Charlie
 *
 */
public abstract class AopLoggingHelper {
  
  private static final ThreadLocal<Object[]> logParameters = new ThreadLocal<Object[]>();
  
  private static final ThreadLocal<Boolean> skipLogging = new ThreadLocal<Boolean>();
  
  private static final Object[] emptyObjArr = new Object[]{};
  
  /**
   * Call this method to reset the helper after current logging to
   * avoid affects next logging.
   */
  static void reset() {
    if(logParameters != null) {
      logParameters.remove();
    }
    if(skipLogging != null) {
      skipLogging.remove();
    }
  }

  /**
   * Get log parameters which are used to format the log message
   * 
   * @return
   */
  public static Object[] getLogParameters() {
    Object[] paras = logParameters.get();
    if(paras == null) {
      return emptyObjArr;
    }
    
    return paras;
  }

  /**
   * Set the log parameters which are used to format the log message
   * 
   * @param logParameters
   */
  public static void setLogParameters(Object... logParameters) {
    AopLoggingHelper.logParameters.set(logParameters);
  }

  /**
   * Test whether to skip current logging
   * 
   * @return
   */
  public static Boolean isSkipLogging() {
    if(skipLogging == null) {
      return false;
    }
    
    Boolean res = skipLogging.get();
    if(res == null) {
      return false;
    }
    
    return res;
  }

  /**
   * Set whether to skip current logging
   * 
   * @param skipLogging
   */
  public static void setSkipLogging(Boolean skipLogging) {
    AopLoggingHelper.skipLogging.set(skipLogging);
  }

}
