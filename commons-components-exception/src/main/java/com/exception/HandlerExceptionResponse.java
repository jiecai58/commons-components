package com.exception;

import com.dto.facade.response.BizResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author caijie
 * @date 2021-8-26 3:18 PM
 */
public abstract class HandlerExceptionResponse implements ExceptionResponse {

    @Override
    public Object throwExceptionInfo(HttpServletRequest request, HttpServletResponse response, long errCode, String errMsg) {
        BizResponse bizResponse = new BizResponse();
        bizResponse.setCode((int) errCode);
        bizResponse.setMessage(errMsg);
        return bizResponse;
    }
}
