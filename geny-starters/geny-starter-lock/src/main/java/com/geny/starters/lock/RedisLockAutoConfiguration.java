package com.geny.starters.lock;

import com.geny.components.redis.lock.core.LockFactory;
import com.geny.components.redis.lock.interceptor.RedisLockInterceptor;
import com.geny.starters.redis.RedissonAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@AutoConfigureAfter(RedissonAutoConfiguration.class)
@Import(RedisLockInterceptor.class)
public class RedisLockAutoConfiguration {

    Logger logger = LoggerFactory.getLogger(RedisLockAutoConfiguration.class);

    @Bean
    LockFactory lockFactory(){

        logger.info("RedisLockAutoConfiguration enabled");

        return new LockFactory();
    }

}
