package com.geny.components.redis.lock.interceptor;

import com.geny.components.redis.lock.annotation.Lockable;
import com.geny.components.redis.lock.core.Lock;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

@Slf4j
public abstract class AbstractLockInterceptor {

    protected abstract Lock getLock(Lockable lockable, String key);

    protected abstract boolean tryLock(Lock lock) throws InterruptedException;

    @Pointcut("@annotation(io.ifa.components.redis.lock.annotation.Lockable)")
    public void pointcut(){}

    //@Around("pointcut()")
    @Around(value = "@annotation(lockable)")
    public Object process(ProceedingJoinPoint point,Lockable lockable) throws Throwable{

        log.debug("拦截到 {} 方法需要加锁",point.getSignature());

        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        String targetName = point.getTarget().getClass().getName();
        String methodName = point.getSignature().getName();
        Object[] args = point.getArgs();

        if(method != null && method.isAnnotationPresent(Lockable.class)){
            //Lockable lockable = method.getAnnotation(Lockable.class);
            //lockable = method.getAnnotation(Lockable.class);

            String key = getLockKey(method,targetName,methodName,lockable.key(),args);
            Lock lock = getLock(lockable, key);

            boolean isLock = lock.lock();
            if(isLock){
                try {
                    return point.proceed();
                }finally {
                    lock.unlock();
                }
            }else {
                throw new RuntimeException("获取资源锁失败");
            }

        }

        return point.proceed();
    }

    private String getLockKey(Method method, String targetName, String methodName, String[] keys, Object[] arguments) {
        StringBuilder sb = new StringBuilder();
        sb.append("lock.").append(targetName).append(".").append(methodName);

        if(keys != null) {
            String keyStr = Joiner.on(".").skipNulls().join(keys);
            if(!StringUtils.isBlank(keyStr)) {
                LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
                String[] parameters =discoverer.getParameterNames(method);
                ExpressionParser parser = new SpelExpressionParser();
                Expression expression = parser.parseExpression(keyStr);
                EvaluationContext context = new StandardEvaluationContext();
                int length = parameters.length;
                if (length > 0) {
                    for (int i = 0; i < length; i++) {
                        context.setVariable(parameters[i], arguments[i]);
                    }
                }
                String keysValue = expression.getValue(context, String.class);
                sb.append("#").append(keysValue);
            }
        }
        return sb.toString();
    }
}
