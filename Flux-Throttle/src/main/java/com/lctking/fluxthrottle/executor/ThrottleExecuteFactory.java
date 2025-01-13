package com.lctking.fluxthrottle.executor;

import com.lctking.fluxthrottle.cache.factory.CacheFactory;
import com.lctking.fluxthrottle.constant.CacheTypeEnum;
import com.lctking.fluxthrottle.executor.impl.DistributedThrottleExecuteServiceImpl;
import com.lctking.fluxthrottle.executor.service.DistributedThrottleExecuteService;
import com.lctking.fluxthrottle.executor.service.ThrottleExecuteService;

import java.util.NoSuchElementException;

public class ThrottleExecuteFactory {
    private static DistributedThrottleExecuteServiceImpl DISTRIBUTED_THROTTLE_EXECUTE_SERVICE;

    private static final String DISTRIBUTED_THROTTLE_EXECUTE_SERVICE_INITIAL_LOCK = "distributed_throttle_execute_service_initial_lock";

    public static ThrottleExecuteService getInstance(CacheTypeEnum cacheTypeEnum){
        switch (cacheTypeEnum){
            case REDIS, DISTRIBUTED -> {
                return getDistributedThrottleExecuteService();
            }
            default -> throw new NoSuchElementException("no such element:"+cacheTypeEnum);
        }
    }

    private static DistributedThrottleExecuteService getDistributedThrottleExecuteService(){
        if(DISTRIBUTED_THROTTLE_EXECUTE_SERVICE != null)return DISTRIBUTED_THROTTLE_EXECUTE_SERVICE;
        synchronized (DISTRIBUTED_THROTTLE_EXECUTE_SERVICE_INITIAL_LOCK){
            if(DISTRIBUTED_THROTTLE_EXECUTE_SERVICE != null)return DISTRIBUTED_THROTTLE_EXECUTE_SERVICE;
            DISTRIBUTED_THROTTLE_EXECUTE_SERVICE = new DistributedThrottleExecuteServiceImpl(CacheFactory.getInstance(CacheTypeEnum.DISTRIBUTED));
            return DISTRIBUTED_THROTTLE_EXECUTE_SERVICE;
        }
    }
}
