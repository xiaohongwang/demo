package com.ehcache.service.impl;

import com.ehcache.service.EhCacheService;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Cacheable
 */
@Service
public class EhCacheServiceImpl implements EhCacheService {
    @Resource
    private CacheManager cacheManager;

    public String get(String key) {
      return  (String) cacheManager.getCache("labelCache").get(key).get();
    }

    @Cacheable(value = "labelCache", key = "#param")
    public String put(String param) {
        List<Integer> data = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            data.add(i);
        }
        data.stream().forEach(e -> System.out.println(e));
        return "success";
    }
}
