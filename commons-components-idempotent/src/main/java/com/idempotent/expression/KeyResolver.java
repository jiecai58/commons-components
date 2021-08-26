package com.idempotent.expression;
import com.idempotent.annotation.Idempotent;
import org.aspectj.lang.JoinPoint;

/**
 * 唯一标志处理器
 *
 * @author Dingkeke
 * @date 2021-08-09 10:07
 */
public interface KeyResolver {

    /**
     * 解析处理key
     *
     * @param idempotent 幂等接口注解标识
     * @param joinpoint  接口切点信息
     * @return 处理结果
     */
    String resolver(Idempotent idempotent, JoinPoint joinpoint);
}
