package com.example;
public class InputValidator {
    public static boolean isYes(String input) {
        return input.equalsIgnoreCase("y");
    }
    public static boolean isNo(String input) {
        return input.equalsIgnoreCase("n");
    }
}