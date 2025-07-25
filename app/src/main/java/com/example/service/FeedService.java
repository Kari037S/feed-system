package com.example.service;

import com.example.PublisherApp;
import com.example.ReaderApp;
import com.example.SubscriptionManager;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FeedService {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SubscriptionManager subscriptionManager = new SubscriptionManager();

    public void startPublisher() {
        System.out.print("Enter Blog Name to Publish: ");
        String blogName = scanner.nextLine().trim();


        System.out.print("Enter Author Name: ");
        String authorName = scanner.nextLine().trim();

        Thread publisherThread = new Thread(new PublisherApp(blogName, authorName));
        publisherThread.start();
    }

    public void startReader() {
        System.out.print("Enter Reader Name: ");
        String readerName = scanner.nextLine().trim();
        List<String> subscriptions = subscriptionManager.loadSubscriptions(readerName);

        while (true) {
            System.out.println("\n📖 Your Subscribed Blogs: " + subscriptions);
            System.out.println("➕ Press 'a' to Add Channel");
            System.out.println("➖ Press 'r' to Remove Channel");
            System.out.println("▶️ Press 'read' to Read a Blog");
            System.out.println("❌ Press 'e' to Exit");

            System.out.print("Enter your choice: ");
            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
                case "a" -> {
                    List<String> allAvailable = subscriptionManager.getAllAvailableChannels();
                    List<String> unsubscribed = allAvailable.stream()
                            .filter(blog -> !subscriptions.contains(blog))
                            .collect(Collectors.toList());

                    System.out.println("📜 Available Blogs: " + unsubscribed);
                    System.out.print("Type blog name to subscribe or 'e' to exit: ");
                    String blogToAdd = scanner.nextLine().trim();
                    if (!blogToAdd.equalsIgnoreCase("e") && unsubscribed.contains(blogToAdd)) {
                        subscriptions.add(blogToAdd);
                        subscriptionManager.saveSubscriptions(readerName, subscriptions);
                        System.out.println("✅ Subscribed to " + blogToAdd);
                    } else if (!blogToAdd.equalsIgnoreCase("e")) {
                        System.out.println("❌ Blog not found or already subscribed.");
                    }
                }

                case "r" -> {
                    if (subscriptions.isEmpty()) {
                        System.out.println("⚠️ You have no subscriptions to remove.");
                        break;
                    }
                    System.out.print("Type blog name to remove or 'e' to exit: ");
                    String blogToRemove = scanner.nextLine().trim();
                    if (subscriptions.contains(blogToRemove)) {
                        subscriptions.remove(blogToRemove);
                        subscriptionManager.saveSubscriptions(readerName, subscriptions);
                        System.out.println("✅ Removed " + blogToRemove + " from subscriptions.");
                    } else if (!blogToRemove.equalsIgnoreCase("e")) {
                        System.out.println("❌ Blog is not in your list. Try again.");
                    }
                }

                case "read" -> {
                    System.out.print("Enter Blog name to read: ");
                    String blogToRead = scanner.nextLine().trim();
                    if (subscriptions.contains(blogToRead)) {
                        Thread readerThread = new Thread(new ReaderApp(readerName, blogToRead));
                        readerThread.start();
                        System.out.println("🔁 Reading blog... Press 'e' anytime in terminal to exit reader.");
                    } else {
                        System.out.println("❌ Blog not subscribed.");
                    }
                }

                case "e" -> {
                    System.out.println("👋 Exiting Feed System...");
                    return;
                }

                default -> System.out.println("❗ Invalid option. Try again.");
            }
        }
    }
}
