package com.example;

import java.nio.file.Path;
import java.util.Scanner;

public class PublisherApp implements Runnable {
    private final String blogName;
    private final String authorName; // ✅ New field

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
            FeedSystemUtils.ensureDirectoryExists("Blogs");  // ✅ ensure Blogs/ folder exists

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("\nEnter Blog content (or type 'exit now' to quit): ");
                String content = scanner.nextLine().trim();
                if (content.equalsIgnoreCase("exit now")) break;

                // ✅ Pass actual author name, not blogName
                Article article = new Article(authorName, blogName, content);
                channel.publish(article); // publish to in-memory subscribers

                // ✅ Write to Blogs/ folder so ReaderApp can detect changes
                Path blogFile = FeedSystemUtils.getBlogFilePath(blogName);
                FeedSystemUtils.writeToFile(blogFile, authorName + "|" + content);

                FeedSystemUtils.printPublished(blogName);  // ✅ feedback
            }

            scanner.close();
        } catch (Exception e) {
            FeedSystemUtils.printError("Error in publishing: " + e.getMessage());
        }
    }
}
