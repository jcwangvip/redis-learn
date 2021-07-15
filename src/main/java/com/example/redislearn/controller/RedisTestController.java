package com.example.redislearn.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 测试类
 */
@RestController
@RequestMapping("/redis")
@AllArgsConstructor
public class RedisTestController {

    private final RedisTemplate<String, String> redisTemplate;


    @DeleteMapping
    public Boolean delete(@RequestParam("key") String key) {
        Boolean delete = redisTemplate.delete(key);
        System.out.println("delete = " + delete);
        return delete;
    }

    @GetMapping
    public String get(@RequestParam("key") String key) {
        String value = redisTemplate.opsForValue().get(key);
        System.out.println("value = " + value);
        return value;
    }

    @PostMapping
    public Boolean set(@RequestParam("key") String key, @RequestParam("value") String value) {
        redisTemplate.opsForValue().set(key, value);
        return Boolean.TRUE;
    }

    /**
     * 保存key存在的,个人理解想到与更新
     *
     * @param key   key
     * @param value value
     * @return 保存的key已经存在就更新返回true、key不存在返回false
     */
    @PostMapping("/setIfPresent")
    public Boolean setIfPresent(@RequestParam("key") String key, @RequestParam("value") String value) {
        Boolean result = redisTemplate.opsForValue().setIfPresent(key, value);
        System.out.println("setIfAbsent result = " + result);
        return result;
    }

    /**
     * 设值如果没有当前key保存
     *
     * @param key   key
     * @param value value
     * @return 有key返回false、没有key返回ture
     */
    @PostMapping("/setIfAbsent")
    public Boolean setIfAbsent(@RequestParam("key") String key, @RequestParam("value") String value) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value);
        System.out.println("setIfAbsent result = " + result);
        return result;
    }


}
