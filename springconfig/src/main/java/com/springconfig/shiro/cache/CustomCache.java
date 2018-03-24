package com.springconfig.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangmin on 2018/3/24.
 */
public class CustomCache<K, V> implements Cache<K, V> {

    private long expireTime = 1800;// 缓存的超时时间，单位为s

    private RedisTemplate<K, V> redisTemplate;// 通过构造方法注入该对象

    public CustomCache() {
        super();
    }

    public CustomCache(long expireTime, RedisTemplate<K, V> redisTemplate) {
        this.expireTime = expireTime;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public V get(K key) throws CacheException {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public V put(K key, V value) throws CacheException {
        redisTemplate.opsForValue().set(key, value, this.expireTime, TimeUnit.SECONDS);
        return value;
    }

    @Override
    public V remove(K key) throws CacheException {
        V v = redisTemplate.opsForValue().get(key);
        redisTemplate.opsForValue().getOperations().delete(key);
        return v;
    }

    @Override
    public void clear() throws CacheException {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<K> keys() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }
}
