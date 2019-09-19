package com.jinnjo.sale.config;

import com.jinnjo.base.util.StringUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedissonSpringDataConfig {
    @Value("${spring.redis.host}")
    private String hostName;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxTotal;
    @Value("${spring.redis.database17}")
    private int index17;
    @Value("${spring.redis.jedis.pool.max-wait}")
    private int maxWaitMillis;

    @Primary
    @Bean(name = "redis0")
    public StringRedisTemplate stringRedisTemplate0(RedissonClient redisson0){
        StringRedisTemplate temple = new StringRedisTemplate();
        temple.setConnectionFactory(new RedissonConnectionFactory(redisson0));
        return temple;
    }

    @Bean(name = "redis17")
    public StringRedisTemplate stringRedisTemplate17(RedissonClient redisson17){
        StringRedisTemplate temple = new StringRedisTemplate();
        temple.setConnectionFactory(new RedissonConnectionFactory(redisson17));
        return temple;
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson0() {
        return Redisson.create(configRedisson(0));
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson17() {
        return Redisson.create(configRedisson(index17));
    }

    private Config configRedisson(int database) {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress(String.format("redis://%s:%d",hostName, port));
        singleServerConfig.setConnectionMinimumIdleSize(maxIdle);
        singleServerConfig.setConnectionPoolSize(maxTotal);
        if(maxWaitMillis > 0) {
            singleServerConfig.setIdleConnectionTimeout(maxWaitMillis);
        }
        if(StringUtil.isNotEmpty(password)) {
            singleServerConfig.setPassword(password);
        }
        singleServerConfig.setDatabase(database);
        return config;
    }
}