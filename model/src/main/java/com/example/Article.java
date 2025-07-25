package com.example;

public class Article {
    private final String author;
    private final String channelName;
    private final String content;

    public Article(String author, String channelName, String content) {
        this.author = author;
        this.channelName = channelName;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Author: " + author +
                "\nChannel: " + channelName +
                "\nContent: " + content;
    }
}
