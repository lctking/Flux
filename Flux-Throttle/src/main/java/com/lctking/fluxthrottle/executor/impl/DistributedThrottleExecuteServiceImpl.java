package com.lctking.fluxthrottle.executor.impl;

import com.lctking.fluxthrottle.annotation.Throttle;
import com.lctking.fluxthrottle.cache.service.CacheService;
import com.lctking.fluxthrottle.executor.ThrottleArgsWrapper;
import com.lctking.fluxthrottle.executor.service.DistributedThrottleExecuteService;
import com.lctking.fluxthrottle.utils.ExceptionThrower;
import com.lctking.fluxthrottle.utils.SpELParser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

@RequiredArgsConstructor
public class DistributedThrottleExecuteServiceImpl implements DistributedThrottleExecuteService {
    private final CacheService cacheService;
    @Override
    public void proceed(ProceedingJoinPoint joinPoint, Throttle throttle) {
        execute(wrapperBuilder(joinPoint, throttle));
    }

    @SneakyThrows
    @Override
    public void execute(ThrottleArgsWrapper wrapper) {
        Throttle throttle = wrapper.getThrottle();
        String keyForLock = wrapper.getKeyForLock();
        Boolean setIfAvailable = cacheService.setIfAvailable(keyForLock, throttle.threshold(), throttle.windowSize(), throttle.timeUnit());
        if(setIfAvailable == null || !setIfAvailable){
            Class<? extends Throwable> exceptionClass = throttle.exceptionClass();
            ExceptionThrower.throwException(exceptionClass, throttle.message());
        }
    }

    public ThrottleArgsWrapper wrapperBuilder(ProceedingJoinPoint joinPoint, Throttle throttle){
        Object parsedValue = SpELParser.parse(throttle.spEL(), ((MethodSignature) joinPoint.getSignature()).getMethod(), joinPoint.getArgs());
        String spELValue = "";
        try{
            spELValue = (String) parsedValue;
        }catch (ClassCastException e){
            // 如果parsedValue是基本数据类型，则使用String.valueOf
            spELValue = String.valueOf(parsedValue);
        }
        String keyForLock = throttle.uniquePrefix()+":"+spELValue+":"+throttle.uniqueSuffix();

        return ThrottleArgsWrapper.builder()
                .joinPoint(joinPoint)
                .throttle(throttle)
                .keyForLock(keyForLock)
                .build();
    }
}
