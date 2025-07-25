package com.example;

public class ReaderApp implements Runnable {
    private final String readerName;
    private final String channelToSubscribe;

    public ReaderApp(String readerName, String channelToSubscribe) {
        this.readerName = readerName;
        this.channelToSubscribe = channelToSubscribe;
    }

    @Override
    public void run() {
        start();
    }

    public void start() {
        // ✅ Always create or get the channel — safe for different JVMs
        Channel channel = InMemoryChannelRegistry.getOrCreateChannel(channelToSubscribe);

        Reader reader = new Reader() {
            @Override
            public void receive(Article article) {
                FeedSystemUtils.printReceivedArticle(readerName, channelToSubscribe, article);
            }

            @Override
            public String getName() {
                return readerName;
            }
        };

        // ✅ Subscribe and start watching
        FeedSystemUtils.subscribeReader(reader, channel);
        System.out.println("👀 Reader is watching blog: " + channelToSubscribe);
        FeedSystemUtils.startWatchingArticles(readerName, channel);
    }
}
