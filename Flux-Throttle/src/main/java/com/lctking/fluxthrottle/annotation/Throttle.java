package com.lctking.fluxthrottle.annotation;

import com.lctking.fluxthrottle.exception.ThrottleException;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Throttle {
    String message() default "限流中";

    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    String spEL() default "";

    /**
     * 特殊前缀
     */
    String uniquePrefix() default "";

    /**
     * 特殊后缀
     */
    String uniqueSuffix() default "";

    /**
     * 最大请求数
     */
    long threshold() default 500L;

    /**
     * 窗口大小（毫秒）
     */
    long windowSize() default 1000L;

    Class<? extends Throwable> exceptionClass() default ThrottleException.class;
}
