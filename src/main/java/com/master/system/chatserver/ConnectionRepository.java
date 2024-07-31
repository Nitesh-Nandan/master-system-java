package com.master.system.chatserver;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConnectionRepository {
    private final Map<String, CustomSocket> connections = new HashMap<>();

    public void addConnection(String key, CustomSocket socket) {
        connections.put(key, socket);
    }

    public boolean isAvailable(String key) {
        return connections.containsKey(key);
    }

    public CustomSocket getConnection(String key) {
        return connections.getOrDefault(key, null);
    }

    public void removeConnection(String key) {
        connections.remove(key);
    }

    public Set<String> getClientList() {
        return connections.keySet();
    }
}
