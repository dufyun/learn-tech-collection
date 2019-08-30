## 项目学习总结
> 本项目是自己参与公司项目，根据公司项目中使用的技术点进行整理，将核心的技术点整理成Micr—Demo（最小化demo）示例！

主要会包括以下的内容：
#### 1、 spring-resty的搭建和使用
描述：其中会使用到简单的验证校验功能！


#### 2、 cache-to-file-db 缓存数据写入文件-文件数据入库
> 数据异步入库

描述：通过将缓存中写入文件中，然后操作文件进行数据的入库！

核心：
- 缓存写入文件
- 文件的扫描（定时扫描文件）
- 读取文件数据进行入库，并备份文件



#### 3、common-exception-msg 包括公共的异常处理小demo

PropertiesUtil ： 获取properties属性内容的工具类，有六种方式获取属性文件
BaseServiceException ： 自定义异常
BaseMessageCode：自定义消息码



---

相约未来