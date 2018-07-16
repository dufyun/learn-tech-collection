## spring-resty项目


#### 搭建过程简述

1.添加依赖的jar包
> 详情可以pom文件
> 使用了 Hutool 工具包

2.配置webxml
>

3.配置application.xml


4.配置logback


5.写demo


6.写测试类


## resty添加cookie 验证功能
1. 新增工具类和配置文件
2. 新增filter
3. 测试

```
# 错误提示
{"name":"dufy"}
{"retCode":"400002","retMsg":"令牌传递错误或者未携带令牌"}

```




## 参考
[resteasy文档](http://docs.jboss.org/resteasy/docs/2.2.1.GA/userguide/html/)

[spring和resteasy集成，构建restful风格服务](http://blog.csdn.net/skmbw/article/details/12352365)


