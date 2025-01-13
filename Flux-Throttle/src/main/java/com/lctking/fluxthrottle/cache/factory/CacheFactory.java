package com.lctking.fluxthrottle.cache.factory;

import com.lctking.fluxthrottle.cache.impl.DistributeCacheServiceImpl;
import com.lctking.fluxthrottle.cache.service.DistributeCacheService;
import static cn.hutool.extra.spring.SpringUtil.getBean;
public class CacheFactory {
    private static DistributeCacheService DISTRIBUTE_CACHE_SERVICE;

    private static final String DISTRIBUTE_CACHE_INITIAL_LOCK = "distribute_cache_initial_lock";


    public static DistributeCacheService getInstance(){
        if(DISTRIBUTE_CACHE_SERVICE != null)return DISTRIBUTE_CACHE_SERVICE;
        synchronized (DISTRIBUTE_CACHE_INITIAL_LOCK){
            if(DISTRIBUTE_CACHE_SERVICE != null)return DISTRIBUTE_CACHE_SERVICE;
            DISTRIBUTE_CACHE_SERVICE = getBean(DistributeCacheServiceImpl.class);
            return DISTRIBUTE_CACHE_SERVICE;
        }
    }
}
