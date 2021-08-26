package com.cache.aspect;


import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author Jie.cai58@gmail.com
 * @date 2019/9/19 14:20
 */

public class SpelParser {
    private static ExpressionParser parser = new SpelExpressionParser();

    public static String getKey(String key, String condition, String[] paramNames, Object[] arguments) {
        try {
            if (!checkCondition(condition, paramNames, arguments)) {
                return null;
            }
            Expression expression = parser.parseExpression(key);
            return expression.getValue(doSpelParser(paramNames, arguments), String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getKey(String key, String[] paramNames, Object[] arguments) {
        Expression expression = parser.parseExpression(key);
        return expression.getValue(doSpelParser(paramNames, arguments), Object.class);
    }

    public static boolean checkCondition(String condition, String[] paramNames, Object[] arguments) {
        if (condition.length() < 1) {
            return true;
        }
        Expression expression = parser.parseExpression(condition);
        return expression.getValue(doSpelParser(paramNames, arguments), boolean.class);
    }

    private static EvaluationContext doSpelParser(String[] paramNames, Object[] arguments) {
        /**
         * 解析为el表达式
         */
        EvaluationContext context = new StandardEvaluationContext();
        int length = paramNames.length;
        if (length <= 0) {
            return null;
        }
        for (int i = 0; i < length; i++) {
            context.setVariable(paramNames[i], arguments[i]);
        }
        return context;
    }

}
