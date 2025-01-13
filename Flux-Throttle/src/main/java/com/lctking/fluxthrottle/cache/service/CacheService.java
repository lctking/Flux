package com.lctking.fluxthrottle.cache.service;

import java.util.concurrent.TimeUnit;

public interface CacheService{
    Boolean setIfAvailable(String key, long threshold, long windowSize, TimeUnit timeUnit);
}
