package com.geny.components.redis.lock.annotation;

import com.geny.components.redis.lock.core.LockMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Lockable {

    String[] key() default "";

    long maxWait() default 2000;

    long expiration() default 1000;

    LockMode mode() default LockMode.Fair;

    TimeUnit unit() default TimeUnit.MILLISECONDS;
}
