package com.exception;


import com.exception.BaseException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ResponseHandler
 *
 * @author caijie
 * @date 2021-8-19 3:18 PM
 */
@Component
public class ExceptionHandler {

    @Resource
    private com.exception.ExceptionResponse<Object> exceptionResponse;

    /**
     * 指定返回Response
     *
     * @param errCode 错误码
     * @param errMsg  错误信息
     * @return Object
     */
    public Object handle(HttpServletRequest request, HttpServletResponse response, long errCode, String errMsg) {
        return exceptionResponse.throwExceptionInfo(request, response, errCode, errMsg);
    }


    public Object handle(HttpServletRequest request, HttpServletResponse response, BaseException e) {
        return handle(request, response, e.getErrorCode(), e.getMessage());
    }
}
