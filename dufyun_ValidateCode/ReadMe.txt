					dufy learn SpringMVC first day
1.创建一个动态的Web的项目 名字为dufyun_SpringMVC
2.在web-inf 下创建一个Lib文件夹，存放所需要的jar包，具体jar包信息如下：(有些可能不是必须的，暂时用不到)
		aopalliance.jar
		aspectjweaver.jar
		cglib-nodep-2.1_3.jar
		commons-logging.jar
		jstl.jar
		servlet-api.jar
		standard.jar
		spring-aop-4.0.2.RELEASE.jar
		spring-aspects-4.0.2.RELEASE.jar
		spring-beans-4.0.2.RELEASE.jar
		spring-context-4.0.2.RELEASE.jar
		spring-core-4.0.2.RELEASE.jar
		spring-expression-4.0.2.RELEASE.jar
		spring-jdbc-4.0.2.RELEASE.jar
		spring-orm-4.0.2.RELEASE.jar
		spring-test-4.0.2.RELEASE.jar
		spring-tx-4.0.2.RELEASE.jar
		spring-web-4.0.2.RELEASE.jar
		spring-webmvc-4.0.2.RELEASE.jar
3.修改Web.xml 和添加spring-servlet.xml 
	具体的配置见详细文件内容说明，spring-servlet.xml可以参考文档说明
		!-- HandlerMapper 请求的URL和 Bean名字映射-->
         <bean class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping"></bean>
        <!-- HandlerAdapter -->
         <bean class="org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter"></bean>
        
        <!-- 配置Controller -->
        <bean name="/hello" class="com.dufyun.springmvc.web.controller.HelloController"></bean>
        
        <!-- viewResolver -->
        <!-- 支持Servlet 、jsp 视图解析-->
        <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        	<property name="viewClass" value="org.springframework.web.servlet.view.JstlView"/>
        	<property name="prefix" value="/WEB-INF/jsp/" />
        	<property name="suffix" value=".jsp" />
        </bean> 

4.新建一个controller的包 并写第一个HelloController 的控制器 进行HelloWorld的输入
5.创建jsp页面
6.启动项目：输入http://8080/项目名/hello  测试环境的搭建
7.在spring-servlet.xml中配置字符集编码格式，防止编码乱的问题

1、 前端控制器DispatcherServlet； 
2、 HandlerMapping 
3、 HandlerAdapter 
4、 ViewResolver 
5、 处理器/页面控制器 
6、 视图 

===============================second day  quartz =============================
1.引入jar spring-support.jar, quartz-all-2.0.2.jar  调用的是org.springframework.scheduling.quartz.QuartzJobBean
2.定义作业类 Job1
	org.springframework.scheduling.quartz.JobDetailBean
	 ①：jobClass   java代码中定义的任务类
	 ②：jobDataAsMap   该任务类中需要注入的属性值
3.配置作业类的触发器
	①:org.springframework.scheduling.quartz.SimpleTriggerBean  按照一定频度进行，间隔多久
	②：org.springframewolk.scheduling.quartz.CronTriggerBean  按照指定的时间进行
	 

