package com.exception;


/**
 *
 * @author Jie.cai58@gmail.com
 * @date 2014/9/19 21:15
 */
public abstract class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private long code;

    public BaseException() {
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(long code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(long code, String message, Throwable cause) {
        super(message);
        this.code = code;
        super.initCause(cause);
    }

    public long getErrorCode() {
        return this.code;
    }

    public void setErrorCode(long code) {
        this.code = code;
    }
}

