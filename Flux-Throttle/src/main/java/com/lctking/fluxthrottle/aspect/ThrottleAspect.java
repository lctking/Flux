package com.lctking.fluxthrottle.aspect;

import com.lctking.fluxthrottle.annotation.Throttle;
import com.lctking.fluxthrottle.executor.ThrottleExecuteFactory;
import com.lctking.fluxthrottle.executor.service.ThrottleExecuteService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static cn.hutool.extra.spring.SpringUtil.getBean;

@Aspect
public class ThrottleAspect {
    private static final Map<String, Object> instanceMap = new HashMap<>();

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
            result = doReject(throttle);
            instance.exceptionProcess();
        }
        return result;
    }

    private static Throttle IdempotentGetter(ProceedingJoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //Method targetMethod = joinPoint.getTarget().getClass().getDeclaredMethod(methodSignature.getName(), methodSignature.getMethod().getParameterTypes());
        return methodSignature.getMethod().getAnnotation(Throttle.class);
    }

    private static Object doReject(Throttle throttle){
        String rejectMethod = throttle.rejectMethod();
        int index = rejectMethod.lastIndexOf(".");
        String className = rejectMethod.substring(0,index);
        String methodName = rejectMethod.substring(index+1);
        Object handlerInstance = getHandlerInstance(className);
        Object rejectResult = null;
        try {
            Method method = handlerInstance.getClass().getMethod(methodName);
            rejectResult = method.invoke(handlerInstance);
        } catch (NoSuchMethodException |InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("fail to do reject");
        }

        return rejectResult;
    }

    private static Object getHandlerInstance(String className) {
        if(instanceMap.containsKey(className)){
            return instanceMap.get(className);
        }
        Object instance = null;
        try {
            instance = getBean(className);
        }catch (Throwable ignored){}
        if (instance != null)return instance;

        try {
            Class<?> clazz = Class.forName(className);
            instance = clazz.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException("fail to find rejectHandler");
        }
        instanceMap.put(className, instance);
        return instance;
    }
}
