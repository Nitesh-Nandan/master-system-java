package com.master.system.bloom;

import redis.clients.jedis.JedisPool;

public class RedisUtil {

    public static JedisPool getLocalJedisPool() {
        return new JedisPool("localhost", 6379);
    }

    public static JedisPool getCentralJedisPool() {
        String host = "redis-19856.c301.ap-south-1-1.ec2.redns.redis-cloud.com";
        int port = 19856;
        String user = "default";
        String password = "2FvoD6CI8dbLJ4HjQf4YtmOnEmoCJghg";
        return new JedisPool(host, port, user, password);
    }
}
