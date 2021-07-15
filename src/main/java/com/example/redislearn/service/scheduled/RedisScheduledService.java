package com.example.redislearn.service.scheduled;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
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
public class RedisScheduledService {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisScheduledService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Value("${server.port}")
    private String port;

//    @Scheduled(cron = "0 */1 * * * ?")
    public void testRedisScheduled() {
        String scheduleName = "test";
        try {
            LocalDateTime localDateTime = LocalDateTime.now();
            String dateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime);
            String label = port + "#" + dateTime;
            if (!scheduledLook(scheduleName, label)) {
                log.info("锁定任务失败,label = {}", label);
                return;
            }
            log.info("成功进入任务,label = {}", label);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scheduledUnLook(scheduleName);
        }
        log.info("finally ...");
    }

    public Boolean scheduledLook(String scheduleName, String label) {
        return redisTemplate.opsForValue().setIfAbsent(scheduleName, label);
    }

    public Boolean scheduledUnLook(String scheduleName) {
        return redisTemplate.delete(scheduleName);
    }

}
