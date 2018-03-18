package org.spring.learnspringbootsession.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 配置RedisHttpSession
 *
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/3/18
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
@Configuration
//maxInactiveIntervalInSeconds: 设置Session失效时间，
// 使用Redis Session之后，原Boot的server.session.timeout属性不再生效
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60)
public class RedisHttpSessionConfig {
}
