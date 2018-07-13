package org.learn.rest.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Logging the execution of all dubbo service methods via {@code AspectJ}
 * 
 * @author Charlie
 *
 */
@Aspect
@Component
public class AopLogging {
	
  private static final Logger logger = LoggerFactory.getLogger(AopLogging.class);
  
  ThreadLocal<Long> time = new ThreadLocal<Long>();
  
  
  @Before("execution(public * org.learn.rest.resource.*Resouce.*(..))")
  public void beforeExecution(JoinPoint joinPoint){
    time.set(System.currentTimeMillis());
  }
  
  @SuppressWarnings("rawtypes")
  @AfterReturning(value="execution(public * org.learn.rest.resource.*Resouce.*(..))", returning="ret")
  public void afterReturning(JoinPoint joinPoint, Object ret){

  }
  
  @AfterThrowing(value="execution(public * org.learn.rest.resource.*Resouce.*(..))", throwing="e")
  public void afterThrowing(JoinPoint joinPoint, Throwable e){

  }
  
  @After("execution(public * org.learn.rest.resource.*Resouce.*(..))")
  public void afterExecution(JoinPoint joinPoint) {
    AopLoggingHelper.reset();
  }

  private void log(JoinPoint joinPoint, Object ret, String retCode, String retMsg) {
    
    if(AopLoggingHelper.isSkipLogging()) {
      return;
    }
    
    MethodSignature ms = (MethodSignature) joinPoint.getSignature();
    Method method = ms.getMethod();
    Log logAnnotated = method.getAnnotation(Log.class);

  }

}
