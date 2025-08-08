package com.example;

import java.nio.file.Path;
import java.util.Scanner;

public class PublisherApp implements Runnable {
    private final String blogName;
    private final String authorName; 

    public PublisherApp(String blogName, String authorName) {
        this.blogName = blogName;
        this.authorName = authorName;
    }

    @Override
    public void run() {
        start();
    }

    public void start() {
        Channel channel = InMemoryChannelRegistry.getOrCreateChannel(blogName);
        FeedSystemUtils.printPublisherStarted(blogName);

        try {
            FeedSystemUtils.ensureDirectoryExists("Blogs");  

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("\nEnter Blog content (or type 'exit now' to quit): ");
                String content = scanner.nextLine().trim();
                if (content.equalsIgnoreCase("exit now")) break;

                Article article = new Article(authorName, blogName, content);
                channel.publish(article); 

                Path blogFile = FeedSystemUtils.getBlogFilePath(blogName);
                FeedSystemUtils.writeToFile(blogFile, authorName + "|" + content);

                FeedSystemUtils.printPublished(blogName);  
            }

            scanner.close();
        } catch (Exception e) {
            FeedSystemUtils.printError("Error in publishing: " + e.getMessage());
        }
    }
}
