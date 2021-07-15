package com.example.redislearn.dto;

import com.example.redislearn.look.LockObject;
import lombok.Data;

/**
 * 类描述
 *
 * @author jcwang
 */
@Data
public class UserDTO implements LockObject {

    private String name;

    @Override
    public String lockPath() {
        return name;
    }


}
