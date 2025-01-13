package com.lctking.fluxthrottle.config;

import com.lctking.fluxthrottle.aspect.ThrottleAspect;
import com.lctking.fluxthrottle.cache.impl.DistributedCacheServiceImpl;
import com.lctking.fluxthrottle.executor.impl.DistributedThrottleExecuteServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@RequiredArgsConstructor
public class ThrottleAutoConfiguration {
    private final StringRedisTemplate stringRedisTemplate;

    @Bean
    public DistributedCacheServiceImpl distributeCacheService(){
        return new DistributedCacheServiceImpl(stringRedisTemplate);
    }

    @Bean
    public DistributedThrottleExecuteServiceImpl throttleExecuteService(DistributedCacheServiceImpl distributeCacheService){
        return new DistributedThrottleExecuteServiceImpl(distributeCacheService);
    }

    @Bean
    public ThrottleAspect throttleAspect(){
        return new ThrottleAspect();
    }
}
