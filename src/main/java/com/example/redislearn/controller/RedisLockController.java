package com.example.redislearn.controller;

import com.example.redislearn.dto.UserDTO;
import com.example.redislearn.service.RedisLockService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * redis测试锁
 *
 * @author jcwang
 */
@RestController
@RequestMapping("/redis/lock")
@AllArgsConstructor
public class RedisLockController {

    private final RedisLockService redisLockService;


    @PostMapping("saveUser")
    public String saveUser(@RequestBody UserDTO user) {
        return redisLockService.saveUser(user);
    }


}
