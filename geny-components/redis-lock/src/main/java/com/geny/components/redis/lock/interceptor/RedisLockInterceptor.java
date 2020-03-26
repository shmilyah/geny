package com.geny.components.redis.lock.interceptor;

import com.geny.components.redis.lock.annotation.Lockable;
import com.geny.components.redis.lock.core.Lock;
import com.geny.components.redis.lock.core.LockFactory;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Aspect
@Component
public class RedisLockInterceptor extends AbstractLockInterceptor {

    @Resource
    LockFactory lockFactory;

    protected Lock getLock(Lockable lockable, String key) {
        return lockFactory.getLock(lockable,key);
    }

    protected boolean tryLock(Lock lock) throws InterruptedException {
        return lock.lock();
    }
}
