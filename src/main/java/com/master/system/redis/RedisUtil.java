package com.master.system.redis;

import redis.clients.jedis.JedisPool;

public class RedisUtil {

    public static JedisPool getCentralJedisPool() {
        String host = "redis-15123.c241.us-east-1-4.ec2.redns.redis-cloud.com";
        int port = 15123;
        String user = "default";
        String password = "B4Y5Daew4839o1Cs66yuzsrjux8IpNXW";
        return new JedisPool(host, port, user, password);
    }
}
