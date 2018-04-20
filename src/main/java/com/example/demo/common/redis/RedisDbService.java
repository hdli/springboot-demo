package com.example.demo.common.redis;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hdli on 2018-4-20.
 */
@Service
public class RedisDbService {

    private Map<Integer, StringRedisTemplate> cacheMap = new HashMap<>();

    public StringRedisTemplate getRedisTemplate(Integer indexDb, StringRedisTemplate redisTemplate){
        StringRedisTemplate redis = cacheMap.get(indexDb);
        if (redis != null){
            return redis;
        }
        JedisConnectionFactory jedisConnectionFactory = (JedisConnectionFactory)redisTemplate.getConnectionFactory();
        jedisConnectionFactory.setDatabase(indexDb);
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        cacheMap.put(indexDb,redisTemplate);
        return redisTemplate;
    }
}
