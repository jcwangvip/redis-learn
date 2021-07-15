package com.example.redislearn.service.impl;

import com.example.redislearn.dto.UserDTO;
import com.example.redislearn.look.Locked;
import com.example.redislearn.service.RedisLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 类描述
 *
 * @author jcwang
 */
@Slf4j
@Component
public class RedisLockServiceImpl implements RedisLockService {


    @Locked
    @Override
    public String saveUser(UserDTO user) {
        log.info("用户进入{}", user.getName());
        return user.getName();
    }

    @Override
    public String syncLock(String path) {
        log.info("path = {}", path);
        return path;
    }

}
