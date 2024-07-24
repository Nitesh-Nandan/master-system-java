package com.master.system.bloom;

import com.master.system.redis.JedisCommandImpl;
import com.master.system.redis.RedisCommand;
import com.master.system.redis.RedisUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class RedisBloomFilterTest {
    private static final String BLOOM_KEY = "TEST_BLOOM";

    @BeforeEach
    void setUp() {
        RedisCommand redisCommand = new JedisCommandImpl(RedisUtil.getCentralJedisPool());
        redisCommand.deleteKey(BLOOM_KEY);
    }

    @Test
    void testBloomFilter() {
        IBloomFilter bloomFilter = new RedisBloomFilter(BLOOM_KEY, 0.01, 100);
        Assertions.assertThat(bloomFilter.addKey("Nitesh")).isTrue();
        Assertions.assertThat(bloomFilter.isKeyExist("Nitesh")).isTrue();
        Assertions.assertThat(bloomFilter.isKeyExist("Nandan")).isFalse();
    }

}