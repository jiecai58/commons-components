package com.idempotent.exception;

/**
 * 幂等异常
 *
 * @author Dingkeke
 * @date 2021-08-09 10:51
 */
public class IdempotentException extends RuntimeException {

    private String code;

    private String errorMessage;


    public IdempotentException() {
        super();
    }

    /**
     * 参数构造器
     *
     * @param message 错误信息
     * @param code    错误码
     */
    public IdempotentException(String code, String message) {
        super(message);
        this.errorMessage = message;
        this.code = code;
    }

    /**
     * 带参数构造器
     *
     * @param message 错误信息
     * @param cause   错误原因
     */
    public IdempotentException(String message, Throwable cause) {
        super(message, cause);
        this.errorMessage = message;
    }

    /**
     * 带参数构造器
     *
     * @param message 错误信息
     */
    public IdempotentException(String message) {
        super(message);
        this.errorMessage = message;
    }

    public IdempotentException(Throwable cause) {
        super("", cause);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
