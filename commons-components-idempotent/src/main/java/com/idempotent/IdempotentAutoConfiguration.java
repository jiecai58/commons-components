package com.idempotent;

import com.idempotent.aspect.IdempotentAspect;
import com.idempotent.cache.IdempotentCache;
import com.idempotent.cache.RenaultIdempotentCache;
import com.idempotent.expression.IdempotentKeyResolver;
import com.idempotent.properties.IdempotentProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Dingkeke
 * @date 2021-08-06 18:05
 */
@Configuration
@EnableConfigurationProperties({IdempotentProperties.class})
@ConditionalOnProperty(prefix = IdempotentProperties.PREFIX,name = "enable",havingValue = "true")
public class IdempotentAutoConfiguration {
    /**
     * 切面 拦截处理所有 @Idempotent
     * @return Aspect
     */
    @Bean
    public IdempotentAspect idempotentAspect() {
        return new IdempotentAspect();
    }

    /**
     * key 解析器
     * @return KeyResolver
     */
    @Bean
    @ConditionalOnMissingBean(KeyResolver.class)
    public KeyResolver keyResolver() {
        return new IdempotentKeyResolver();
    }

    @Bean
    @ConditionalOnProperty(prefix = IdempotentProperties.PREFIX,name = "cache.impl",havingValue = "renault",matchIfMissing = true)
    public IdempotentCache idempotentCache(IdempotentProperties idempotentProperties){
        return new RenaultIdempotentCache(idempotentProperties.getCacheName());
    }
}
