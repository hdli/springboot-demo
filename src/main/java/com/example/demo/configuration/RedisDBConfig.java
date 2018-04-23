package com.example.demo.configuration;

import com.alibaba.druid.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by hdli on 2018-4-22.
 */
@Configuration
public class RedisDBConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.pool.max-active}")
    private int poolMaxActive;
    @Value("${spring.redis.pool.max-wait}")
    private int poolMaxWait;
    @Value("${spring.redis.pool.max-idle}")
    private int poolMaxIdle;
    @Value("${spring.redis.pool.min-idle}")
    private String poolMixIdle;
    @Value("${spring.redis.timeout}")
    private String timeout;

    @Bean(name = "redis0")
    public StringRedisTemplate redisTemplate(){

        return credrtRedisTemplate(0);
    }

    @Bean(name = "redis1")
    public StringRedisTemplate redisTemplate1(){

        return credrtRedisTemplate(1);
    }

    public StringRedisTemplate credrtRedisTemplate(int index){
        StringRedisTemplate temple = new StringRedisTemplate();
        temple.setConnectionFactory(connectionFactory(host, port, password,
                poolMaxIdle, poolMaxActive, index, poolMaxWait, true));
        return temple;
    }

    public RedisConnectionFactory connectionFactory(String hostName, int port,
                                                    String password, int maxIdle, int maxTotal, int index,
                                                    long maxWaitMillis, boolean testOnBorrow) {
        JedisConnectionFactory jedis = new JedisConnectionFactory();
        jedis.setHostName(hostName);
        jedis.setPort(port);
        if (!StringUtils.isEmpty(password)) {
            jedis.setPassword(password);
        }
        if (index != 0) {
            jedis.setDatabase(index);
        }
        jedis.setPoolConfig(poolCofig(maxIdle, maxTotal, maxWaitMillis,
                testOnBorrow));

        // 初始化连接pool
        jedis.afterPropertiesSet();
        RedisConnectionFactory factory = jedis;

        return factory;
    }

    public JedisPoolConfig poolCofig(int maxIdle, int maxTotal,
                                     long maxWaitMillis, boolean testOnBorrow) {
        JedisPoolConfig poolCofig = new JedisPoolConfig();
        poolCofig.setMaxIdle(maxIdle);
        poolCofig.setMaxTotal(maxTotal);
        poolCofig.setMaxWaitMillis(maxWaitMillis);
        poolCofig.setTestOnBorrow(testOnBorrow);
        return poolCofig;
    }

}
