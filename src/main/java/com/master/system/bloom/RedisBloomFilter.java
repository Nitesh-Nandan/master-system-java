package com.master.system.bloom;

public class RedisBloomFilter implements IBloomFilter {

    public RedisBloomFilter(String key, int capacity, float errorRate) {

    }

    @Override
    public boolean addKey(String key) {
        return false;
    }

    @Override
    public boolean isKeyExist(String key) {
        return false;
    }
}
