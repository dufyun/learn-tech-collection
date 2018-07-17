## cache-to-file-db  项目
> 整个项目的核心是 数据的异步入库，异步入库的数据是实时性要求不是特别高的场景！保证数据的最终一致性！

快速操作：
1. 执行项目中的数据库初始化sql，在project.properties配置自己mysql信息！
2. 启动redis的服务！
3. 启动 AppMain , 然后观察数据中变化！

## 整个项目包结构说明

#### org.dufy
- annotation : 自定义注解
- cache ： 缓存操作的包
- dao ： 数据库操作的包
- file2db ： 文件入库
- listener ： 监听器
- log ： 日志文件操作
- model ： 实体
- service ： 业务
- task ： 任务
- util ： 工具包
 
AppMain ： 启动类

#### resources
- spring-config  
applicationContext.xml : spring 公共的配置
spring_cache.xml ： 缓存的配置
spring_mybatis.xml ： mybatis配置

- file2db.yaml ： 文件入库的配置
- FileTransfer.yaml ： 文件扫描和转移的配置
- logback.xml ： 日志的配置
- machine.properties ： 转移机器日志格配置（多机部署情况）
- project.properties ： 项目的属性配置



## 搭建过程
1. 添加对应的jar包
> 详情jar包配置参考pom文件

2. spring-mybatis的配置和对数据库的操作
- （1）需要的核心jar：mysql的jar，spring-mybaits的jar，dataSouce的jar等等
- （2）添加配置文件spring-mybatis.xml
- （3）添加db.properties
- （4）使用 mybatis-generator自动生成代码（User、UserDao、UserMapper）
- （5）使用 `org.dufy.dao.UserDaoTest` 进行mybaits的测试

````
# 进行测试插入和查询，下面是查询返回数据
User{id=1, userName='测试', password='sfasgfaf', age=24}
User{id=2, userName='测试11', password='sfasgfaf', age=24}
User{id=3, userName='dufy', password='123456', age=25}
User{id=4, userName='github', password='123456', age=10}

````
3. 缓存相关的配置
- （1）添加对应的jar ，jedis 、spring-data-redis等
- （2）添加cache对应的配置文件，既spring-cache.xml
- （3）添加`RedisClientTemplate` redis操作工具类
- （4）进行测试，`org.dufy.cache.RedisClientTemplateTest`
4. 设计缓存的入库缓存保存的key
两种方式：
- （1）使用String
> key=user:id
> value=json(user).toString
- （2）使用Hash
> key=user:id
> key "name" "dufy"
> key "age" 25

注： 需要redis配置集群，可参考[Redis创建高可用集群教程【Windows环境】](https://blog.csdn.net/u010648555/article/details/79427608)

5. 日志入库文件的扫描和转移
- （1）日志的配置logback和 DateLogger
- （2）自定义rollPolicy的org.dufy.log.logback
- （3）配置FileTransfer.yaml以及 transfer
- （4）使用定时器生成User日志--spring-task

6. 读取文件数据并进行入库操作
- （1）文件入库操作配置 machine.properties 和 file2db.yaml 
> 注意 yaml 格式
- （2）扫描和入库代码 org.dufy.file2db


## 启动服务

> 启动类 `org.dufy.AppMain`


## test包下的类介绍

org.dufy
- cache ： 缓存的测试类
- dao ：dao的测试类
- listener ： spring监听的测试类  

BaseCaseTest ： 测试基类

## 附：有一个spring Listener 的测试的代码

1. 测试的代码`org.dufy.listener`

2. 测试类`org.dufy.listener.DemoListenerTest`

- ApplicationListener
> 监听Spring容器的启动

-  ServletContextListener
> 监听容器的启动和关闭。如果是tomcat需要在web.xml中配置！