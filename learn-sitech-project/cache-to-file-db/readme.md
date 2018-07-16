## cache-to-file-db  项目
> 整个项目的核心是 数据的异步入库，异步入库的数据是实时性要求不是特别高的场景！保证数据的最终一致性！


## 整个项目包结构说明



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


4. 日志入库文件的扫描和转移


5. 读取文件数据并进行入库操作



