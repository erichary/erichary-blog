package com.erichary.blog.exception;

import org.springframework.util.CollectionUtils;

import java.util.Objects;

/**
 * 异常封装
 *
 * @author erichary
 */
public class MyException extends RuntimeException {
    public MyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyException(String message) {
        super(message);
    }
}
