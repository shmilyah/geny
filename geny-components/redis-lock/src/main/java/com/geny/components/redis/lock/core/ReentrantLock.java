package com.geny.components.redis.lock.core;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * @Description: 可冲入锁
 */
public class ReentrantLock extends AbstractLock{

    private RLock lock;

    public ReentrantLock(RedissonClient redissonClient) {
        super(redissonClient);
    }

    public boolean lock() {
        try {
            lock = redissonClient.getLock(key);
            return lock.tryLock(lockable.maxWait(), lockable.expiration(), lockable.unit());
        }catch (InterruptedException e){
            return false;
        }
    }

    public void unlock() {
        if(lock.isHeldByCurrentThread()){
            lock.unlinkAsync();
        }
    }
}
