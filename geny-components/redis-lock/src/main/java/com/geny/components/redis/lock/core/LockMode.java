package com.geny.components.redis.lock.core;

/**
 * @Description: 锁类型
 */
public enum LockMode {

    /**
     * 可重入锁
     */
    Reentrant,

    /**
     * 公平锁
     */
    Fair,

    /**
     * 读锁
     */
    Read,

    /**
     * 写锁
     */
    Write;

    LockMode(){}
}
