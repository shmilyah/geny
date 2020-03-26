package com.geny.components.redis.lock.core;

import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;

public class ReadLock extends AbstractLock {

    private RReadWriteLock lock;

    public ReadLock(RedissonClient redissonClient) {
        super(redissonClient);
    }

    public boolean lock() {
        try {
            lock = redissonClient.getReadWriteLock(key);
            return lock.readLock().tryLock(lockable.maxWait(),lockable.expiration(),lockable.unit());
        }catch (InterruptedException e){
            return false;
        }
    }

    public void unlock() {
        if(lock.readLock().isHeldByCurrentThread()){
            lock.readLock().unlockAsync();
        }
    }
}
