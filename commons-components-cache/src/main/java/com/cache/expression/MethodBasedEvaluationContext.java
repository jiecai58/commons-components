package com.cache.expression;

import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author caijie
 */
public class MethodBasedEvaluationContext {

    public static EvaluationContext doSpelParser(MethodSignature methodSignature, Object[] arguments) {
        /**
         * 解析为el表达式
         */
        String[] paramNames = methodSignature.getParameterNames();
        EvaluationContext context = new StandardEvaluationContext();
        int length = (paramNames != null ? paramNames.length : parameterCount(methodSignature));

        if (length <= 0) {
            return null;
        }
        for (int i = 0; i < length; i++) {
            context.setVariable(paramNames[i], arguments[i]);
        }
        return context;
    }

    public static int parameterCount(MethodSignature methodSignature) {
        return methodSignature.getParameterTypes().length;
    }
}
