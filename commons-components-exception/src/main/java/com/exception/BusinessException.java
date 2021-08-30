package com.exception;

import com.extension.enums.BizEnum;

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
        this.message = message;
    }

    public BusinessException(long code, String message, Throwable cause) {
        super(code, message, cause);
        this.message = message;
    }

    public BusinessException(BizEnum bizEnum) {
        this(bizEnum.getCode(), bizEnum.getDesc());
    }

    public BusinessException(BizEnum bizEnum, Throwable cause) {
        this(bizEnum.getCode(), bizEnum.getDesc(), cause);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
