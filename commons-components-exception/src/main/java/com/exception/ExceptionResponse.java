package com.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author caijie
 * @date 2021-8-26 3:18 PM
 */
public interface ExceptionResponse<T> {

    T throwExceptionInfo(HttpServletRequest request, HttpServletResponse response, long errCode, String errMsg);
}
