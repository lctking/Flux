package com.lctking.fluxthrottle.cache.impl;

import com.lctking.fluxthrottle.cache.service.DistributedCacheService;
import com.lctking.fluxthrottle.constant.ThrottleResultEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class DistributedCacheServiceImpl implements DistributedCacheService {
    private final StringRedisTemplate stringRedisTemplate;

    private static final String LUA_SCRIPT_RATE_LIMIT_IF_AVAILABLE_PATH = "lua/rate_limit_set_if_available.lua";
    @Override
    public Boolean setIfAvailable(String key, long threshold, long windowSize, TimeUnit timeUnit) {
        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        ClassPathResource resource = new ClassPathResource(LUA_SCRIPT_RATE_LIMIT_IF_AVAILABLE_PATH);
        redisScript.setScriptSource(new ResourceScriptSource(resource));
        redisScript.setResultType(String.class);

        long windowSizeMillis = timeUnit.toMillis(windowSize);
        long currentTimeMillis = System.currentTimeMillis();
        String resultStr = stringRedisTemplate.execute(redisScript, List.of(key), String.valueOf(threshold), String.valueOf(windowSizeMillis), String.valueOf(currentTimeMillis));
        return resultStr != null && !resultStr.equals(ThrottleResultEnum.FAIL.value);
    }
}
