package com.example.redislearn.service.scheduled;

import com.example.redislearn.look.ScheduledLocked;
import com.example.redislearn.look.redisson.LockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 类描述
 *
 * @author jcwang
 */
@Slf4j
@Service
public class ScheduledByLockService {


    private final LockService lockService;


    @Value("${server.port}")
    private String port;

    public ScheduledByLockService(LockService lockService) {
        this.lockService = lockService;
    }

    @ScheduledLocked
    @Scheduled(cron = "0 */1 * * * ?")
    public void testRedisScheduled() {
        String scheduleName = "test";
        log.info("scheduleName={}开始", scheduleName);
        String dateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        String label = port + "#" + dateTime;
//        lockService.syncLock(() -> log.info("成功进入任务,label = {}", label), Collections.singletonList(scheduleName));
        log.info("scheduleName={}结束", scheduleName);
    }


}
