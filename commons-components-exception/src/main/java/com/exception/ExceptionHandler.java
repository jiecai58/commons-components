package com.exception;


import com.dto.facade.response.BizResponse;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * ResponseHandler
 *
 * @author caijie
 * @date 2021-8-19 3:18 PM
 */
@Component
public class ExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);
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

    /**
     * 派生的response类
     *
     * @param returnType
     * @param errCode
     * @param errMsg
     * @return
     */
    private static Object handleColaResponse(Class returnType, long errCode, String errMsg) {
        try {
            BizResponse response = (BizResponse) returnType.newInstance();
            response.setCode((int) errCode);
            response.setMessage(errMsg);
            return response;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }

    private static boolean isAssignableFrom(Class returnType) {
        return BizResponse.class.isAssignableFrom(returnType);
    }


    private static boolean isColaResponse(Class returnType) {
        return returnType == BizResponse.class || returnType.getGenericSuperclass() == BizResponse.class;
    }

    /**
     * 属性response类
     *
     * @param entity
     * @param values
     * @return
     */
    public static Object handleColaResponse(Class entity, Map<String, Object> values) {
        try {
            Field[] fields = entity.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                String fieldName = fields[i].getName();
                if (values.get(fieldName) == null) {
                    continue;
                }
                fields[i].set(fieldName, values.get(fieldName));
            }
            return fields;
        } catch (SecurityException e) {
            logger.warn("发生异常：" + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return true;
    }

    private static Map<String, Object> setResponseMap(long errCode, String errMsg) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("code", errCode);
        responseMap.put("message", errMsg);
        return responseMap;
    }
}
