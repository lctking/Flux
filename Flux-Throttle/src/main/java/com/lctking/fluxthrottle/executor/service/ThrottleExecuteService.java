package com.lctking.fluxthrottle.executor.service;

import com.lctking.fluxthrottle.annotation.Throttle;
import com.lctking.fluxthrottle.executor.ThrottleArgsWrapper;
import org.aspectj.lang.ProceedingJoinPoint;

public interface ThrottleExecuteService {
    void proceed(ProceedingJoinPoint joinPoint, Throttle throttle);

    void execute(ThrottleArgsWrapper wrapper);

    default void exceptionProcess(){}

    /**
     * 后置处理
     */
    default void postProcess(){}
}
