package com.example;

import com.example.PublisherApp;
import com.example.ReaderApp;
import com.example.SubscriptionManager;
import com.example.service.FeedService;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SubscriptionManager subscriptionManager = new SubscriptionManager();
    private static final FeedService feedService = new FeedService();

    public static void main(String[] args) {
        System.out.println("Welcome to the Feed System!");

        System.out.print("Are you a Publisher (p) or Reader (r)? ");
        String role = scanner.nextLine().trim().toLowerCase();

        switch (role) {
            case "p" -> feedService.startPublisher();
            case "r" -> feedService.startReader();
            default -> System.out.println("Invalid option. Please enter 'p' for Publisher or 'r' for Reader.");
        }
    }
}





