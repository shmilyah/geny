package com.geny.components.redis.lock.core;


import com.geny.components.redis.lock.annotation.Lockable;

public interface Lock {

    Lock init(Lockable lockable, String key);

    boolean lock();

    void unlock();
}
