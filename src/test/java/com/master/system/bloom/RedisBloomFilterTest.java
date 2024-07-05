package com.master.system.bloom;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class RedisBloomFilterTest {

//    @Test
    void testBloomFilter() {
        IBloomFilter bloomFilter = new RedisBloomFilter("TEST_BLOOM", 0.01, 100);
        Assertions.assertThat(bloomFilter.addKey("Nitesh")).isTrue();
        Assertions.assertThat(bloomFilter.isKeyExist("Nitesh")).isTrue();
        Assertions.assertThat(bloomFilter.isKeyExist("Nandan")).isTrue();
    }

}