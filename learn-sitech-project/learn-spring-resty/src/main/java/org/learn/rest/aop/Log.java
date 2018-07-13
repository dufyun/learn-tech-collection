package org.learn.rest.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.text.MessageFormat;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used to customize an AOP method invocation logging.
 * 
 * @author Charlie
 *
 */
@Documented
@Target({METHOD})
@Retention(RUNTIME)
public @interface Log {
  
  /**
   * The logging message.
   * we will use {@link MessageFormat} to format this message, and you should set associated 
   * parameters with AopLoggingHelper
   * 
   * @return
   */
  String message() default "";
  
  LogLevel level() default LogLevel.TRACE;
  
  boolean skipLog() default false;
  
  /**
   * Log level, which has the same meaning with slf4j or other log frameworks.
   * 
   * @author Charlie
   *
   */
  public enum LogLevel {
    
    TRACE("TRACE"),
    
    DEBUG("DEBUG"),
    
    INFO("INFO"),
    
    WARN("WARN"),
    
    ERROR("ERROR");
    
    private String value;
    
    LogLevel(String value) {
      this.value = value;
    }
    
    public String value() {
      return this.value;
    }
  }

}
