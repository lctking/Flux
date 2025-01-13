package com.lctking.fluxthrottle.cache.factory;

import com.lctking.fluxthrottle.cache.impl.DistributeCacheServiceImpl;
import com.lctking.fluxthrottle.cache.service.CacheService;
import com.lctking.fluxthrottle.cache.service.DistributeCacheService;
import com.lctking.fluxthrottle.constant.CacheTypeEnum;
import java.util.NoSuchElementException;
import static cn.hutool.extra.spring.SpringUtil.getBean;

public class CacheFactory {
    private static DistributeCacheService DISTRIBUTE_CACHE_SERVICE;

    private static final String DISTRIBUTE_CACHE_INITIAL_LOCK = "distribute_cache_initial_lock";


    public static CacheService getInstance(CacheTypeEnum cacheTypeEnum){
        switch (cacheTypeEnum){
            case REDIS, DISTRIBUTED -> {
                return getDistributeCacheService();
            }
            default -> throw new NoSuchElementException("no such element:"+cacheTypeEnum);
        }
    }

    public static DistributeCacheService getDistributeCacheService(){
        if(DISTRIBUTE_CACHE_SERVICE != null)return DISTRIBUTE_CACHE_SERVICE;
        synchronized (DISTRIBUTE_CACHE_INITIAL_LOCK){
            if(DISTRIBUTE_CACHE_SERVICE != null)return DISTRIBUTE_CACHE_SERVICE;
            DISTRIBUTE_CACHE_SERVICE = getBean(DistributeCacheServiceImpl.class);
            return DISTRIBUTE_CACHE_SERVICE;
        }
    }
}
