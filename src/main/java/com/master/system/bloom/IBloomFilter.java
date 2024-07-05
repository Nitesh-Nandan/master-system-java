package com.master.system.bloom;

public interface IBloomFilter {

    boolean addKey(String key);

    boolean isKeyExist(String key);
}
