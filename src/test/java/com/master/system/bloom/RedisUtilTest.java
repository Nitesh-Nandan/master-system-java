package com.master.system.bloom;

import org.assertj.core.api.Assertions;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@ExtendWith(SpringExtension.class)
class RedisUtilTest {

    private JedisPool localRedisPool;
    private JedisPool centralRedisPool;
    private EasyRandom generator;

    @BeforeEach
    void setUp() {
        localRedisPool = RedisUtil.getLocalJedisPool();
        centralRedisPool = RedisUtil.getCentralJedisPool();
        generator = new EasyRandom();
    }

    @Test
    void testLocalRedisConnection() {
        Assertions.assertThat(localRedisPool).isNotNull();
        Jedis client = localRedisPool.getResource();
        Assertions.assertThat(client).isNotNull();

        String key = generator.nextObject(String.class);
        String value = generator.nextObject(String.class);

        client.set(key, value);
        Assertions.assertThat(value).isEqualTo(client.get(key));
    }


    @Test
    void testCentralRedisConnection() {
        Assertions.assertThat(centralRedisPool).isNotNull();
        Jedis client = centralRedisPool.getResource();
        Assertions.assertThat(client).isNotNull();

        String key = generator.nextObject(String.class);
        String value = generator.nextObject(String.class);

        client.set(key, value);
        Assertions.assertThat(value).isEqualTo(client.get(key));
    }
}