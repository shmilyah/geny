package com.geny.components.redis.lock.core;

import com.geny.components.redis.lock.annotation.Lockable;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

@Slf4j
public class LockFactory {

    @Resource
    private RedissonClient redissonClient;

    private static final Map<LockMode,Lock> locks = Maps.newHashMap();

    @PostConstruct
    public void init(){
        locks.put(LockMode.Fair,new FairLock(redissonClient));
        locks.put(LockMode.Reentrant,new ReentrantLock(redissonClient));
        locks.put(LockMode.Read,new ReadLock(redissonClient));
        locks.put(LockMode.Write,new WriteLock(redissonClient));
        log.info("Distributed lock initialization successful");
    }

    public Lock getLock(Lockable lockable, String key){
        return locks.get(lockable.mode()).init(lockable, key);
    }

}
