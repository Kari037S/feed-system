package com.example;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface FeedSystemUtils {

    static void ensureDirectoryExists(String dir) throws IOException {
        Files.createDirectories(Paths.get(dir));
    }

    static Path getBlogFilePath(String blogName) {
        return Paths.get("Blogs", blogName + ".txt");
    }

    static void writeToFile(Path path, String content) throws IOException {
        Files.writeString(path, content + System.lineSeparator(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    static String readLastLine(Path filePath) throws IOException {
        List<String> lines = Files.readAllLines(filePath);
        return lines.isEmpty() ? "" : lines.get(lines.size() - 1);
    }

    static boolean fileExists(Path path) {
        return Files.exists(path);
    }

    static long getFileSize(Path path) throws IOException {
        return Files.size(path);
    }

    static void printError(String message) {
        System.out.println("❌ Error: " + message);
    }

    static void printReceivedArticle(String readerName, String blogName, Article article) {
        System.out.println("\n📢 [" + readerName + "] received article on " + blogName);
        System.out.println(article);
    }

    static void printSubscribedMessage(String readerName, String blogName) {
        System.out.println("✅ Reader [" + readerName + "] subscribed");
    }

    static void printPublisherStarted(String blogName) {
        System.out.println("🟢 Publisher for blog \"" + blogName + "\" started.");
    }

    static void printPublished(String blogName) {
        System.out.println("✅ Article published to blog: " + blogName);
    }

    // ✅ ADDED METHODS FOR READER
    static void subscribeReader(Reader reader, Channel channel) {
        channel.subscribe(reader);
        printSubscribedMessage(reader.getName(), channel.getName());
    }

    static void startWatchingArticles(String readerName, Channel channel) {
        Thread watcherThread = new Thread(() -> {
            Set<String> receivedArticles = new HashSet<>();
            Path blogFilePath = getBlogFilePath(channel.getName());

            try {
                // ✅ STEP 1: Immediately show last published article (if exists)
                if (fileExists(blogFilePath)) {
                    String lastLine = readLastLine(blogFilePath);
                    if (!lastLine.isBlank() && lastLine.contains("|")) {
                        String[] parts = lastLine.split("\\|", 2);
                        String author = parts[0];
                        String content = parts[1];

                        Article article = new Article(author, channel.getName(), content);
                        for (Reader subscriber : channel.getSubscribers()) {
                            subscriber.receive(article);
                        }
                        receivedArticles.add(lastLine);
                    }
                }

                // ✅ STEP 2: Start watching for new content
                WatchService watchService = FileSystems.getDefault().newWatchService();
                Path blogDir = Paths.get("Blogs");
                blogDir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

                while (true) {
                    WatchKey key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        Path changed = (Path) event.context();

                        // ✅ Case-insensitive filename match
                        if (changed.toString().equalsIgnoreCase(channel.getName() + ".txt")) {
                            try {
                                Thread.sleep(50);  // ✅ wait for file write to complete
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }

                            if (fileExists(blogFilePath)) {
                                String lastLine = readLastLine(blogFilePath);

                                if (!receivedArticles.contains(lastLine) && lastLine.contains("|")) {
                                    String[] parts = lastLine.split("\\|", 2);
                                    String author = parts[0];
                                    String content = parts[1];

                                    Article article = new Article(author, channel.getName(), content);
                                    for (Reader subscriber : channel.getSubscribers()) {
                                        subscriber.receive(article);
                                    }
                                    receivedArticles.add(lastLine);
                                }
                            }
                        }
                    }
                    key.reset();
                }

            } catch (Exception e) {
                printError("Failed to watch articles: " + e.getMessage());
            }
        });

        watcherThread.setDaemon(true);
        watcherThread.start();
    }
}
