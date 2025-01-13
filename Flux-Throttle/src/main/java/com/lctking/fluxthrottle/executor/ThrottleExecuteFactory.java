package com.lctking.fluxthrottle.executor;

import com.lctking.fluxthrottle.cache.factory.CacheFactory;
import com.lctking.fluxthrottle.constant.CacheTypeEnum;
import com.lctking.fluxthrottle.executor.impl.DistributedThrottleExecuteServiceImpl;
import com.lctking.fluxthrottle.executor.service.ThrottleExecuteService;

import java.util.NoSuchElementException;

public class ThrottleExecuteFactory {
    private static final DistributedThrottleExecuteServiceImpl DISTRIBUTED_THROTTLE_EXECUTE_SERVICE;

    static {
        DISTRIBUTED_THROTTLE_EXECUTE_SERVICE = new DistributedThrottleExecuteServiceImpl(CacheFactory.getInstance(CacheTypeEnum.DISTRIBUTED));
    }

    public static ThrottleExecuteService getInstance(CacheTypeEnum cacheTypeEnum){
        switch (cacheTypeEnum){
            case REDIS, DISTRIBUTED -> {
                return DISTRIBUTED_THROTTLE_EXECUTE_SERVICE;
            }
            default -> throw new NoSuchElementException("no such element:"+cacheTypeEnum);
        }
    }
}
