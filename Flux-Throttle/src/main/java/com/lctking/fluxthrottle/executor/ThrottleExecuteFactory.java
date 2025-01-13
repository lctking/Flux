package com.lctking.fluxthrottle.executor;

import com.lctking.fluxthrottle.cache.factory.CacheFactory;
import com.lctking.fluxthrottle.executor.impl.ThrottleExecuteServiceImpl;
import com.lctking.fluxthrottle.executor.service.ThrottleExecuteService;
public class ThrottleExecuteFactory {
    private static final ThrottleExecuteService THROTTLE_EXECUTE_SERVICE;

    static {
        THROTTLE_EXECUTE_SERVICE = new ThrottleExecuteServiceImpl(CacheFactory.getInstance());
    }

    public static ThrottleExecuteService getInstance(){
        return THROTTLE_EXECUTE_SERVICE;
    }
}
