package com.master.system.redis;

import redis.clients.jedis.JedisPool;

public class JedisCommandImpl implements RedisCommand {
    private final JedisPool jedisPool;

    public JedisCommandImpl(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public boolean isKeyExist(String key) {
        return jedisPool.getResource().exists(key);
    }

    @Override
    public String set(String key, String val) {
        return jedisPool.getResource().set(key, val);
    }

    @Override
    public String getValue(String key) {
        return jedisPool.getResource().get(key);
    }

    @Override
    public boolean deleteKey(String key) {
        return jedisPool.getResource().del(key) > 0 ? Boolean.TRUE : Boolean.FALSE;
    }
}
