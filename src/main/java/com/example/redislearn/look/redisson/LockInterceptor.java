package com.example.redislearn.look.redisson;

import com.example.redislearn.look.LockObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    public void lock(ProceedingJoinPoint pjp) {
        log.info("进入锁流程...");
        List<String> lockPaths = getPath(pjp);
        RLock[] rLocks = lockPaths.stream()
                .map(redissonClient::getLock)
                .toArray(RLock[]::new);
        RLock rLock = redissonClient.getMultiLock(rLocks);
        try {
            boolean lockResult = rLock.tryLock(30, 600, TimeUnit.SECONDS);
            if (!lockResult) {
                log.info("加锁失败:锁路径 {}", lockPaths);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private List<String> getPath(ProceedingJoinPoint pjp) {
        List<String> lockPathList = new ArrayList<>();
        Object[] args = pjp.getArgs();
        for (Object arg : args) {
            if (arg instanceof LockObject) {
                LockObject lockObject = (LockObject) arg;
                lockPathList.add(lockObject.lockPath());
            }
            if (arg instanceof Collection) {
                Collection collection = (Collection) arg;
                for (Object o : collection) {
                    if (o instanceof LockObject) {
                        LockObject lockObject = (LockObject) o;
                        lockPathList.add(lockObject.lockPath());
                    }
                }
            }
        }
        return lockPathList;
    }

}
