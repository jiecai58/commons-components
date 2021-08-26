/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cache.interceptor;
import com.cache.expression.MethodBasedEvaluationContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author caijie
 */
@Component
public class CacheAspectSupport {

    public static final ExpressionParser expressionParser = new SpelExpressionParser();

    public static Object getKey(EvaluationContext context, String key) {
        Expression expression = expressionParser.parseExpression(key);
        return expression.getValue(context, Object.class);
    }

    public static EvaluationContext setEvalContext(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return MethodBasedEvaluationContext.doSpelParser(methodSignature, joinPoint.getArgs());
    }

    public static Boolean isConditionPassing(EvaluationContext context, String key) {
        if (StringUtils.hasText(key)) {
            Expression expression = expressionParser.parseExpression(key);
            return condition(expression, context);
        }
        return true;
    }

    protected static boolean condition(Expression expression, EvaluationContext evalContext) {
        return (Boolean.TRUE.equals(expression.getValue(evalContext, Boolean.class)));
    }

}
