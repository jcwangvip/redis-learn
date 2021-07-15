package com.example.redislearn.service;

import com.example.redislearn.dto.UserDTO;

/**
 * 类描述
 *
 * @author jcwang
 */
public interface RedisLockService {

    String saveUser(UserDTO user);

    String syncLock(String path);
}
