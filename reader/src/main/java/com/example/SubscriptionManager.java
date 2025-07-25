package com.example;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SubscriptionManager {
    private static final String BASE_PATH = "subscriptions/";

    public SubscriptionManager() {
        new File(BASE_PATH).mkdirs();
    }

    public List<String> loadSubscriptions(String readerName) {
        Path filePath = Paths.get(BASE_PATH + readerName + ".txt");
        if (!Files.exists(filePath)) return new ArrayList<>();

        try {
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveSubscriptions(String readerName, List<String> blogs) {
        Path filePath = Paths.get(BASE_PATH + readerName + ".txt");
        try {
            Files.write(filePath, blogs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllAvailableChannels() {
        // You can replace this with dynamic fetching logic if needed
        return Arrays.asList("TechTalks", "FoodieCorner", "TravelDiaries", "FitnessZone", "MindMatters");
    }
}
