package com.idempotent.expression;

import com.idempotent.annotation.Idempotent;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * 幂等处理，此处进行对幂等数据生成key，幂等的判断根据key是否存在进行
 *
 * @author Dingkeke
 * @date 2021-08-09 10:11
 */
public class IdempotentKeyResolver implements KeyResolver {

    private static final SpelExpressionParser spelParser = new SpelExpressionParser();

    private static final LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    @Override
    public String resolver(Idempotent idempotent, JoinPoint joinpoint) {
        Object[] arguments = joinpoint.getArgs();
        String[] params = discoverer.getParameterNames(getMethod(joinpoint));
        StandardEvaluationContext context = new StandardEvaluationContext();

        if (params != null && params.length > 0) {
            for (int len = 0; len < params.length; len++) {
                context.setVariable(params[len], arguments[len]);
            }
        }

        String[] keys=idempotent.keys();
        StringBuffer stringBuffer=new StringBuffer();
        for (String subKey:keys){
            Expression expression = spelParser.parseExpression(subKey);
            String value= expression.getValue(context, String.class);
            stringBuffer.append(value);
        }
        return stringBuffer.toString();

    }


    /**
     * 根据切点解析方法信息
     *
     * @param joinPoint 切点信息
     * @return Method 原信息
     */
    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.getDeclaringClass().isInterface()) {
            try {
                method = joinPoint.getTarget().getClass().getDeclaredMethod(joinPoint.getSignature().getName(),
                        method.getParameterTypes());
            } catch (SecurityException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return method;
    }

}
