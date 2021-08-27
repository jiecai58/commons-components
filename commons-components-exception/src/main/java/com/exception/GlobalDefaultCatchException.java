package com.exception;

import com.extension.enums.BizErrorCodeEnum;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author Jie.cai58@gmail.com
 * @date 2014/9/19 20:51
 */
@ControllerAdvice
public class GlobalDefaultCatchException {

    @Resource
    private ExceptionHandler exceptionHandler;

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    public Object exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
        //如果使用了@ResponseStatus改变HTTP响应的状态码，不处理继续抛出
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }
        if (e instanceof BusinessException) {
            return businessExceptionHandler(request, response, (BusinessException) e);
        } else if (e instanceof MethodArgumentNotValidException) {
            return methodArgumentNotValidExceptionHandler(request, response, (MethodArgumentNotValidException) e);
        } else if (e instanceof BindException) {
            return bindExceptionHandler(request, response, (BindException) e);
        } else if (e instanceof MethodArgumentTypeMismatchException) {
            return methodArgumentTypeMismatchExceptionHandler(request, response, (MethodArgumentTypeMismatchException) e);
        } else if (e instanceof MissingServletRequestParameterException) {
            return missingServletRequestParameterExceptionHandler(request, response, (MissingServletRequestParameterException) e);
        }
        return defaultExceptionHandler(request, response, e);
    }

    private Object businessExceptionHandler(HttpServletRequest request, HttpServletResponse response, BusinessException e) {
        return exceptionHandler.handle(request, response, e.getErrorCode(), e.getMessage());
    }

    private Object methodArgumentNotValidExceptionHandler(HttpServletRequest request, HttpServletResponse response,
                                                          MethodArgumentNotValidException e) {
        String errMsg = methodArgumentNotValidExceptionHandler(e.getParameter().getParameterName(), e.getBindingResult());
        return exceptionHandler.handle(request, response, BizErrorCodeEnum.PARAM_IS_NULL.getCode(), errMsg);
    }

    private Object bindExceptionHandler(HttpServletRequest request, HttpServletResponse response, BindException e) {
        String errMsg = methodArgumentNotValidExceptionHandler(e.getObjectName(), e.getBindingResult());
        return exceptionHandler.handle(request, response, BizErrorCodeEnum.INVALID_FORMAT.getCode(), errMsg);
    }

    private Object methodArgumentTypeMismatchExceptionHandler(HttpServletRequest request, HttpServletResponse response,
                                                              MethodArgumentTypeMismatchException e) {
        return exceptionHandler.handle(request, response, BizErrorCodeEnum.PARAM_ERROR.getCode(),
                e.getParameter().getParameterName());
    }

    private Object missingServletRequestParameterExceptionHandler(HttpServletRequest request, HttpServletResponse response,
                                                                  MissingServletRequestParameterException e) {
        return exceptionHandler.handle(request, response, BizErrorCodeEnum.MISSING_APP_KEY.getCode(), e.getParameterName());
    }


    private Object defaultExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        return exceptionHandler.handle(request, response, BizErrorCodeEnum.SYSTEM_ERROR.getCode(), optimizationTips(e));
    }


    private String optimizationTips(Exception e) {
        String message = e.getMessage();
        if (null == message || "".equals(message)) {
            return "";
        }
        //优化友好的页面信息提示
        String[] messages = message.split(System.getProperty("line.separator", "/n"));
        if (messages.length > 0) {
            message = messages[0];
        }
        return message;
    }

    private String methodArgumentNotValidExceptionHandler(String objectName, BindingResult bindingResult) {

        String fieldErrorMessage = "";
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                sb.append(((FieldError) objectError).getField()).append(":").append(objectError.getDefaultMessage()).append("; ");
            }
            fieldErrorMessage = sb.toString();
        }
        return objectName + ", " + fieldErrorMessage;
    }
}
