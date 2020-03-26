package com.geny.components.redis.lock.core;

import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;

public class WriteLock extends AbstractLock {

    private RReadWriteLock lock;

    public WriteLock(RedissonClient redissonClient) {
        super(redissonClient);
    }

    public boolean lock() {
        try {
            lock = redissonClient.getReadWriteLock(key);
            return lock.writeLock().tryLock(lockable.maxWait(),lockable.expiration(),lockable.unit());
        }catch (InterruptedException e){
            return false;
        }
    }

    public void unlock() {
        if(lock.writeLock().isHeldByCurrentThread()){
            lock.writeLock().unlockAsync();
        }
    }
}
