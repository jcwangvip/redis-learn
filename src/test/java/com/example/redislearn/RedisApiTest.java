package com.example.redislearn;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 类描述
 *
 * @author jcwang
 */
@SpringBootTest
public class RedisApiTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @Test
    void get() {
        String name = redisTemplate.opsForValue().get("name");
        Assertions.assertNotNull(name);
    }

    @Test
    void hasKey() {
        Boolean result = redisTemplate.hasKey("name");
        Assertions.assertTrue(result);
    }
}
