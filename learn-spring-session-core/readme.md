spring-session/docs：https://docs.spring.io/spring-session/docs/

## 本系列教程

### [【第一篇】Spring-Session实现Session共享入门教程](http://blog.csdn.net/u010648555/article/details/79459953)

Spring-session ：https://docs.spring.io/spring-session/docs/  
（1）添加依赖  
（2）web.xml    
（3）配置application.xml和Redis    
（4）验证功能  

 参考文章 

 [使用Spring Session和Redis解决分布式Session跨域共享问题](http://blog.csdn.net/xlgen157387/article/details/57406162)
 
 [学习Spring-Session+Redis实现session共享](https://www.cnblogs.com/andyfengzp/p/6434287.html)
 
 [利用spring session解决共享Session问题](http://blog.csdn.net/patrickyoung6625/article/details/45694157)


### [【第二篇】Spring-Session实现Session共享Redis集群方式配置教程](http://blog.csdn.net/u010648555/article/details/79471034)

（1）添加Redis-Cluster 配置  
（2）添加Redis-Sentinel 配置    
（3）添加Redis.properties    
（4）验证功能  

 参考文章 
 
[架构设计之Spring-Session分布式集群会话管理](https://www.cnblogs.com/smallSevens/p/6763114.html)

[spring-session实现分布式集群session的共享](https://www.cnblogs.com/youzhibing/p/7348337.html)





### [【第三篇】Spring-Session实现Session共享实现原理以及源码解析](http://blog.csdn.net/u010648555/article/details/79491988) 

（1）SessionRepositoryFilter和JedisConnectionFactory注册过程
（2）SessionRepositoryFilter添加到FIlterChain
（3）SessionRepositoryFilter拦截过程
（4）SessionRepository保存session数据



```
Redis连接工厂

JedisConnectionFactory  
JredisConnectionFactory  
LettuceConnectionFactory  
SrpConnectionFactory

```


参考博文

[【Spring】Spring Session的简单搭建与源码阅读](https://www.cnblogs.com/nick-huang/p/6986824.html#my_inner_label13)

[利用spring session解决共享Session问题](http://blog.csdn.net/patrickyoung6625/article/details/45694157)

[Spring Session解决分布式Session问题的实现原理](http://blog.csdn.net/xlgen157387/article/details/60321984)

[spring-session简介、使用及实现原理](http://blog.csdn.net/wojiaolinaaa/article/details/62424642)



### Spring boot 之 Spring-session
此项目没有写博文，可以参考[SpringBoot应用之分布式会话](https://segmentfault.com/a/1190000004358410)
1. 添加jar包或者使用[Spring Initializr](http://start.spring.io/)
2. 添加redis的相关配置，地址、端口等
3. 添加RedisHTTPSession配置
4. 启动Redis，启动Application.java