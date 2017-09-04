package org.shaolin.uimaster.page;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession
public class RedisSessionConfig {

	@Bean
    public JedisConnectionFactory connectionFactory() throws Exception {
        return new JedisConnectionFactory();
    }
}
