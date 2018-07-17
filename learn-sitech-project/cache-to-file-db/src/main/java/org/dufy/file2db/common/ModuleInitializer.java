package org.dufy.file2db.common;

import java.util.Set;

import org.dufy.annotation.Module;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import cn.hutool.core.collection.ConcurrentHashSet;

/**
 * Automatically scan {@link org.dufy.annotation.Module} annotated class and startup after bean initialization.
 * Shutdown process will also be automatically started when this bean is destroyed.
 * 
 * @author Charlie
 *
 */
@Component("moduleInitializer")
public class ModuleInitializer implements DisposableBean, BeanPostProcessor  {
  
  private String[] annotationPackages = {"org.dufy.file2db"};
  
  private Set<IModule> modules = new ConcurrentHashSet<IModule>();

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    if (!isMatchPackage(bean)) {
      return bean;
    }

    Class<?> clazz = bean.getClass();
    if (isProxyBean(bean)) {
      clazz = AopUtils.getTargetClass(bean);
    }
    Module module = clazz.getAnnotation(Module.class);
    if (module != null && bean instanceof IModule) {
      IModule m = ((IModule) bean);
      modules.add(m);
      m.startup();
    }
    return bean;
  }

  @Override
  public void destroy() throws Exception {
    for(IModule m : modules) {
      m.shutdown();
    }
  }

  private boolean isMatchPackage(Object bean) {
    if (annotationPackages == null || annotationPackages.length == 0) {
      return true;
    }
    
    Class<?> clazz = bean.getClass();
    if (isProxyBean(bean)) {
      clazz = AopUtils.getTargetClass(bean);
    }
    
    String beanClassName = clazz.getName();
    for (String pkg : annotationPackages) {
      if (beanClassName.startsWith(pkg)) {
        return true;
      }
    }
    return false;
  }

  private boolean isProxyBean(Object bean) {
    return AopUtils.isAopProxy(bean);
  }
}
