package com.extension;


import com.extension.enums.BizEnum;
import com.extension.enums.BizErrorCodeEnum;

import java.io.Serializable;

/**
 * <p>
 * BizException
 * </p>
 *
 * @author caijie
 * @since 2020/2/20 2:10 下午
 */
public class BizException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 2728936692069322518L;

    private BizEnum errorCode;

    private String errorMessage;

    public BizException() {
        super(BizErrorCodeEnum.OPERATION_FAILED.getDesc());
        this.errorCode = BizErrorCodeEnum.OPERATION_FAILED;
        this.errorMessage = errorCode.getDesc();

    }

    public BizException(BizEnum errorCode) {
        super(errorCode.getDesc());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDesc();
    }

    public BizException(BizEnum errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public BizException(BizEnum errorCode, String errorMessage, Throwable exception) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        super.initCause(exception);
    }

    public BizEnum getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorCode(BizEnum errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static boolean isBizException(Throwable exception) {

        return exception instanceof BizException;
    }

    public static boolean isErrorException(BizEnum errorCode) {

        return BizErrorCodeEnum.SYSTEM_ERROR.equals(errorCode) || BizErrorCodeEnum.CALLSERVICCE_ERROR.equals(errorCode)
                || BizErrorCodeEnum.CALL_SERVICE_ERROR.equals(errorCode)
                || BizErrorCodeEnum.URL_REQUEST_ERROR.equals(errorCode)
                || BizErrorCodeEnum.REQUEST_ERROR.equals(errorCode) || BizErrorCodeEnum.PROCESS_FAIL.equals(errorCode);
    }
}
