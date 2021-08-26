package com.exception;

/**
 * @author Jie.cai58@gmail.com
 * @date 2014/9/19 20:49
 */
public class BusinessException extends BaseException {


    private String message;

    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(long code, String message) {
        super(code, message);
    }

    public BusinessException(long code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public BusinessException(BizErrorCodeEnum BizErrorCodeEnum) {
        this(BizErrorCodeEnum.getCode(), BizErrorCodeEnum.getDesc());
    }

    public BusinessException(BizErrorCodeEnum BizErrorCodeEnum, Throwable cause) {
        this(BizErrorCodeEnum.getCode(), BizErrorCodeEnum.getDesc(), cause);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
