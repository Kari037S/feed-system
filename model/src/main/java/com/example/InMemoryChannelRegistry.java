package com.example;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryChannelRegistry {
    private static final Map<String, Channel> channels = new ConcurrentHashMap<>();

    public static Channel getOrCreateChannel(String name) {
        return channels.computeIfAbsent(name, BlogChannel::new);
    }

    public static Channel getChannelByName(String name) {
        return channels.get(name);
    }
}
