package com.master.system.bloom;


import io.rebloom.client.Client;
import redis.clients.jedis.Jedis;

public class RedisBloomFilter implements IBloomFilter {

    private final String bfKey;
    private final Client bloomClient;

    public RedisBloomFilter(String key, double errorRate, long capacity) {
        this.bfKey = key;
        Jedis jedis = new Jedis("localhost", 6379);
        bloomClient = new Client(RedisUtil.getCentralJedisPool());
        bloomClient.createFilter(key, capacity, errorRate);
    }

    @Override
    public boolean addKey(String key) {
        return bloomClient.add(bfKey, key);
    }

    @Override
    public boolean isKeyExist(String key) {
        return bloomClient.exists(bfKey, key);
    }
}
