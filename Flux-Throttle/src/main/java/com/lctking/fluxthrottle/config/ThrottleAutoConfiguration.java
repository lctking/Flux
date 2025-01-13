package com.lctking.fluxthrottle.config;

import com.lctking.fluxthrottle.aspect.ThrottleAspect;
import com.lctking.fluxthrottle.cache.impl.DistributeCacheServiceImpl;
import com.lctking.fluxthrottle.executor.impl.ThrottleExecuteServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@RequiredArgsConstructor
public class ThrottleAutoConfiguration {
    private final StringRedisTemplate stringRedisTemplate;

    @Bean
    public DistributeCacheServiceImpl distributeCacheService(){
        return new DistributeCacheServiceImpl(stringRedisTemplate);
    }

    @Bean
    public ThrottleExecuteServiceImpl throttleExecuteService(DistributeCacheServiceImpl distributeCacheService){
        return new ThrottleExecuteServiceImpl(distributeCacheService);
    }

    @Bean
    public ThrottleAspect throttleAspect(){
        return new ThrottleAspect();
    }
}
