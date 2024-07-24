package com.master.system.redis;

public interface RedisCommand {
    boolean isKeyExist(String key);

    String set(String key, String val);

    String getValue(String key);

    boolean deleteKey(String key);
}
