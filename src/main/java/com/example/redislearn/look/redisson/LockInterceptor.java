package com.example.redislearn.look.redisson;

import com.example.redislearn.look.LockException;
import com.example.redislearn.look.LockObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
    public Object lock(ProceedingJoinPoint pjp) throws Throwable {
        log.info("进入锁流程...");
        List<String> lockPaths = getPath(pjp);
        RLock[] rLocks = lockPaths.stream()
                .map(redissonClient::getLock)
                .toArray(RLock[]::new);
        RLock rLock = redissonClient.getMultiLock(rLocks);
        try {
            log.info("开始加锁,锁路径数量 = {},值 = {}", lockPaths.size(), lockPaths);
            boolean lockResult = rLock.tryLock(30, 600, TimeUnit.SECONDS);
            if (!lockResult) {
                log.info("加锁失败:锁路径 {}", lockPaths);
                throw new LockException("加锁失败", lockPaths.toArray());
            }
            return pjp.proceed();
        } catch (InterruptedException e) {
            log.error("加锁过程异常{}", e);
            throw new LockException("加锁过程异常{}", e.getMessage());
        } catch (Throwable throwable) {
            log.error("加锁后,目标方法执行异常{}", throwable);
            throw throwable;
        } finally {
            log.info("方法执行完毕,释放锁路径数量 = {},值 = {}", lockPaths.size(), lockPaths);
            rLock.unlock();
        }
    }

    private List<String> getPath(ProceedingJoinPoint pjp) {
        List<String> lockPathList = new ArrayList<>();
        Object[] args = pjp.getArgs();
        for (Object arg : args) {
            addPath(pjp, lockPathList, arg);
            if (arg instanceof Collection) {
                Collection collection = (Collection) arg;
                for (Object o : collection) {
                    addPath(pjp, lockPathList, o);
                }
            }
        }
        return lockPathList;
    }

    private void addPath(ProceedingJoinPoint pjp, List<String> lockPathList, Object arg) {
        if (arg instanceof LockObject) {
            LockObject lockObject = (LockObject) arg;
            String lockPath = lockObject.lockPath();
            if (StringUtils.isEmpty(lockPath)) {
                throw new LockException("lockPath 不能使用 null", pjp.getTarget().getClass().getName());
            }
            lockPathList.add(lockPath);
        }
    }

}
