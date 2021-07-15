package com.example.redislearn.look;

/**
 * 类描述
 *
 * @author jcwang
 */
public class LockException extends RuntimeException {

    private Object[] args;

    public LockException(String msg, Object... args) {
        super(msg);
        this.args = args;
    }
}
