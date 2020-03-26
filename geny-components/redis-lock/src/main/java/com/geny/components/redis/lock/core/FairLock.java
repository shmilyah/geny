package com.geny.components.redis.lock.core;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

public class FairLock extends AbstractLock {

    private RLock lock;

    public FairLock(RedissonClient redissonClient) {
        super(redissonClient);
    }

    public boolean lock() {
        try {
            lock = redissonClient.getFairLock(key);
            return lock.tryLock(lockable.maxWait(),lockable.expiration(),lockable.unit());
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void unlock() {
        if(lock.isHeldByCurrentThread()){
            lock.unlockAsync();
        }
    }
}
