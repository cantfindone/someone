package com.superjoy.someone.exception;

import lombok.Data;

/**
 * @author Ping
 */
@Data
public class CommonException extends RuntimeException {

    private int code;

    public CommonException() {
        super();
    }

    public CommonException(String message, int code) {
        super(message);
        this.code = code;
    }
}
