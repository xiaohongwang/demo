package com.ehcache;

import com.ehcache.service.EhCacheService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by xiaohong on 2017/11/8.
 */
@SpringBootApplication
@RestController
//开启缓存注解
@EnableCaching
public class SpringBootStarter {
    public static void main(String[] args){
        SpringApplication.run(SpringBootStarter.class,args);
    }

    @RequestMapping("/hello")
    public String hello(){
        return "Hello Spring Boot";
    }


    @Resource
    private EhCacheService ehCacheService;

    @RequestMapping(value = "/put/{param}", method = RequestMethod.GET)
    public String put(@PathVariable("param") String param){
        return ehCacheService.put(param);
    }

    @RequestMapping(value = "/get/{param}", method = RequestMethod.GET)
    public String get(@PathVariable("param") String param){
        return ehCacheService.get(param);
    }
}
