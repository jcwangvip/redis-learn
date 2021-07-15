package com.example.redislearn.look.redisson;

import com.example.redislearn.look.LockException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 锁拦截器
 *
 * @author jcwang
 */
@Aspect
@Slf4j
@Component
@AllArgsConstructor
public class LockInterceptor {


    private final RedissonClient redissonClient;

    @Around("execution(* com.example.redislearn..*.*(..)) && @annotation(com.example.redislearn.look.Locked) ")
    public Object lock(ProceedingJoinPoint pjp) throws Throwable {
        log.info("进入锁流程...");
        LockHelper lockHelper = LockHelper.builder().redissonClient(redissonClient).build();
        List<String> lockPaths = lockHelper.getPath(pjp);
        RLock rLock = lockHelper.getRLock(lockPaths);
        try {
            lockHelper.tryLock(lockPaths, rLock);
            return pjp.proceed();
        } catch (InterruptedException e) {
            log.error("加锁过程异常{}", e);
            throw new LockException("加锁过程异常{}", e.getMessage());
        } catch (Throwable throwable) {
            log.error("加锁后,目标方法执行异常{}", throwable);
            throw throwable;
        } finally {
            lockHelper.unlock(lockPaths, rLock);
        }
    }


}
