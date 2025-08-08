package com.example;

import java.util.ArrayList;
import java.util.List;

public class Channel {
    private final String name;
    private final List<Reader> subscribers = new ArrayList<>();

    public Channel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void subscribe(Reader reader) {
        subscribers.add(reader);
    }

    public List<Reader> getSubscribers() {
        return subscribers;
    }

    public void publish(Article article) {
    }
}
