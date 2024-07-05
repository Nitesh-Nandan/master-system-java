package com.master.system.bloom;

import io.rebloom.client.Client;

public class RedisBloomFilterV2 {
    private String bfKey;
    private Client bloomClient;

    public RedisBloomFilterV2(String key, double errorRate, long capacity) {
        this.bfKey = key;
        bloomClient = new Client(RedisUtil.getCentralJedisPool());
        bloomClient.createFilter(key, capacity, errorRate);
    }

    public void addItem(String item) {
        bloomClient.add(bfKey, item);
    }

    public boolean checkItem(String item) {
        return bloomClient.exists(bfKey, item);
    }

    public static void main(String[] args) {
        RedisBloomFilterV2 filter = new RedisBloomFilterV2("myBloomFilter", 0.01, 10000);
        filter.addItem("item1");
        filter.addItem("item2");

        System.out.println("item1 exists: " + filter.checkItem("item1")); // true
        System.out.println("item2 exists: " + filter.checkItem("item2")); // true
        System.out.println("item3 exists: " + filter.checkItem("item3")); // false
    }
}
