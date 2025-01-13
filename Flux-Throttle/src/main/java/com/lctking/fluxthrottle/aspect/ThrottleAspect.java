package com.lctking.fluxthrottle.aspect;

import com.lctking.fluxthrottle.annotation.Throttle;
import com.lctking.fluxthrottle.executor.ThrottleExecuteFactory;
import com.lctking.fluxthrottle.executor.service.ThrottleExecuteService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class ThrottleAspect {
    @Around("@annotation(com.lctking.fluxthrottle.annotation.Throttle)")
    public Object handle(ProceedingJoinPoint joinPoint){
        Throttle throttle = IdempotentGetter(joinPoint);
        Object result = null;
        ThrottleExecuteService instance = ThrottleExecuteFactory.getInstance(throttle.cacheType());

        try{
            instance.proceed(joinPoint,throttle);
            result = joinPoint.proceed();
            instance.postProcess();
        } catch (Throwable e) {
            instance.exceptionProcess();
            throw new RuntimeException(e);
        }
        return result;
    }

    private static Throttle IdempotentGetter(ProceedingJoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //Method targetMethod = joinPoint.getTarget().getClass().getDeclaredMethod(methodSignature.getName(), methodSignature.getMethod().getParameterTypes());
        return methodSignature.getMethod().getAnnotation(Throttle.class);
    }
}
