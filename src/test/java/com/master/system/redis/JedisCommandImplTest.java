package com.master.system.redis;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class JedisCommandImplTest {
    private RedisCommand redisCommand;

    @BeforeEach
    void setUp() {
        redisCommand = new JedisCommandImpl(RedisUtil.getCentralJedisPool());
    }

    @Test
    void testInsertion() {
        Assertions.assertThat(redisCommand.set("nitesh", "nandan")).isEqualTo("OK");
        Assertions.assertThat(redisCommand.isKeyExist("nitesh")).isTrue();
        Assertions.assertThat(redisCommand.getValue("nitesh")).isEqualTo("nandan");
        Assertions.assertThat(redisCommand.deleteKey("nitesh")).isTrue();
        Assertions.assertThat(redisCommand.isKeyExist("nitesh")).isFalse();
        Assertions.assertThat(redisCommand.getValue("nitesh")).isNull();
    }
}