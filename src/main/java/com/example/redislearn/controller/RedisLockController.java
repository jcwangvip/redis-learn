package com.example.redislearn.controller;

import com.example.redislearn.dto.UserDTO;
import com.example.redislearn.look.redisson.LockService;
import com.example.redislearn.service.RedisLockService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    private final LockService lockService;


    @PostMapping("/saveUser")
    public String saveUser(@RequestBody UserDTO user) {
        return redisLockService.saveUser(user);
    }

    @PostMapping("/syncLock/{path}")
    public String syncLock(@PathVariable("path") String path) {
        String result = lockService.syncLock(() -> redisLockService.syncLock(path), path);
        return result;
    }

    @PostMapping("/syncLock")
    public String syncLock(@RequestBody UserDTO user) {
        String result = lockService.syncLock(() -> redisLockService.saveUser(user), user.getName());
        return result;
    }


}
