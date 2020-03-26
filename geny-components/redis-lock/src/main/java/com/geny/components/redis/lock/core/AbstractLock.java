package com.geny.components.redis.lock.core;

import com.geny.components.redis.lock.annotation.Lockable;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractLock implements Lock{

    Logger logger = LoggerFactory.getLogger(getClass());

    protected Lockable lockable;
    protected String key;
    protected RedissonClient redissonClient;

    public AbstractLock(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public Lock init(Lockable lockable, String key) {
        this.lockable = lockable;
        this.key = key;
        return this;
    }

}
