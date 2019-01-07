## EhCache 是一个纯Java的进程内缓存框架，具有快速、精干等特点
## 特点
~~~
1. 快速 基于内存 在速度上要优于redis内存数据库，无需进行网络连接
3. 多种缓存策略
4. 缓存数据有两级：内存和磁盘，因此无需担心容量问题
5. 缓存数据会在虚拟机重启的过程中写入磁盘
6. 可以通过RMI、可插入API等方式进行分布式缓存
7. 具有缓存和缓存管理器的侦听接口
8. 支持多缓存管理器实例，以及一个实例的多个缓存区域
9. 提供Hibernate的缓存实现
~~~
## ehcache 和 redis 比较
~~~
1、ehcache直接在jvm虚拟机中缓存，速度快，效率高；但是缓存共享麻烦，集群分布式应用不方便。
2、redis是通过socket访问到缓存服务，效率比ehcache低，比数据库要快很多，
3、处理集群和分布式缓存方便，有成熟的方案。如果是单个应用或者对缓存访问要求很高的应用，用ehcache。
    如果是大型系统，存在缓存共享、分布式部署、缓存内容很大的，建议用redis
~~~
## Ehcache 参数配置
 |参数|说明|
 |:---:|:---:|
 |name| 缓存名称|
 |maxElementsInMemory|缓存中允许创建的最大对象数|
 |eternal|缓存中对象是否为永久的. true 对象永不过期 如果是，钝化时间\超时时间设置将被忽略|
 |timeToLiveSeconds|单位 秒 ; 生存时间。元素从构建到消亡的最大时间间隔值，如果该值是0就意味着元素可以生存无穷长的时间|
 |timeToIdleSeconds|单位 秒 ; 钝化时间，也就是在一个元素消亡之前，两次访问时间的最大时间间隔值。如果该值是 0 就意味着元素可以停顿无穷长的时间|
 |生存/钝化时间|同时设置 生存、钝化时间时，时间较短者生效||
 |memoryStoreEvictionPolicy|淘汰策略|

## Ehcache相关知识
 - Cache容器对象: CacheManager 管理着（添加或删除）Cache的生命周期
 - Cache: 一个Cache可以包含多个Element，并被CacheManager管理。它实现了对缓存的逻辑行为
 - Element: 需要缓存的元素，它维护着一个键值对， 元素也可以设置有效期，0代表无限制
## SpringBoot 整合 Ehcache
~~~
1、添加依赖
```
<!--Spring boot默认使用的是SimpleCacheConfiguration，即使用ConcurrentMapCacheManager来实现缓存-->
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-cache</artifactId>
      </dependency>
```
2、设置ehcache 配置信息
```
spring:
  cache:
    ehcache:
      config: ehcache.xml
#Spring Boot会自动 扫描路径配置，配置EhCacheCacheMannager的Bean
```
3、使用
```
@Resource
    private CacheManager cacheManager;

    public String get(String key) {
      return  (String) cacheManager.getCache("labelCache").get(key).get();
    }
## Spring 整合ehcache
- 初始化容器对象
```
<bean id="cacheManager" class="org.springframework.cache.ehcache.EhCacheCacheManager">
		<property name="cacheManager">
			<bean class="org.springframework.cache.ehcache.EhCacheManagerFactoryBean">
				<property name="configLocation" value="classpath:conf/ehcache/ehcache.xml" />
			</bean>
		</property>
	</bean>
```

## Ehcache缓存注解
```
1、@Cacheable(value="cacheTest1",key="#param")  //请求后会缓存，查询用,value是配置的cache名，key就表示key
2、@CacheEvict(value="cacheTest2",key="#param") //请求后会删除。删除用！
3、@CachePut(value="cacheTest3",unless="#result==null")//请求后方法肯定执行，并且存缓存，更新用! unless除了结果不等于空。(#result 代表返回结果)
```
## Ehcache 问题
- Ehcache 不支持为某个缓存数据设置生存时间，对于不同缓存数据，需要提供Cache设置
- Ehcache 不支持删除整个Cache中的数据
```
    <!--永不过期-->
    <cache name="foreverCache"
           maxElementsInMemory="100000"
           eternal="true"
           timeToIdleSeconds="0"
           timeToLiveSeconds="0"
           overflowToDisk="false"
           diskPersistent="false"
           diskExpiryThreadIntervalSeconds="120"
           memoryStoreEvictionPolicy="LRU"/>

    <!--labelCache  用户缓存每天的访客信息 设置过期时间 -->
    <cache name="labelCache"
           maxElementsInMemory="100000"
           eternal="false"
           timeToIdleSeconds="0"
           timeToLiveSeconds="172800"
           overflowToDisk="false"
           diskPersistent="false"
           diskExpiryThreadIntervalSeconds="120"
           memoryStoreEvictionPolicy="LRU"/>
```