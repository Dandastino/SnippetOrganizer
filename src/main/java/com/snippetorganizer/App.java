package com.snippetorganizer;

import java.io.IOException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        int choice = -1;
        SnippetManager manager = new SnippetManager();

        try (Scanner scanner = new Scanner(System.in)) {
            while (choice != 0) {
                System.out.print("""
                    --- CODE SNIPPET ORGANIZER ---
                        1. Add new snippet
                        2. Search snippets
                        3. Browse by category
                        4. Edit snippet
                        5. Delete snippet
                        6. Export snippets
                        0. Close application
                    Select an option: """);
                
                try {
                    if (scanner.hasNextInt()) {
                        choice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                    } else {
                        System.out.println("Please insert a valid number.");
                        scanner.next(); 
                        continue;
                    }

                    switch (choice) {
                        case 0 -> {return;}
                        case 1 -> {
                            try {
                                System.out.print("Title: ");
                                String title = getNonEmptyInput(scanner, "Title");

                                System.out.print("Language: ");
                                String language = getNonEmptyInput(scanner, "Language");

                                System.out.println("Enter the code (end with \"X\"): ");
                                StringBuilder codeBuilder = new StringBuilder();
                                while (true) {
                                    String line = scanner.nextLine();
                                    if ("X".equals(line)) break;
                                    codeBuilder.append(line).append(System.lineSeparator());
                                }
                                String code = codeBuilder.toString();
                                if (code.trim().isEmpty()) {
                                    throw new IllegalArgumentException("Code cannot be empty");
                                }
                                
                                manager.addSnippet(title, language, code);
                                System.out.println("Snippet added successfully!");
                            } catch (IllegalArgumentException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        }
                        case 2 -> {
                            System.out.print("Enter search keyword: ");
                            String keyword = scanner.nextLine();
                            manager.searchSnippets(keyword);
                        }
                        case 3 -> {
                            try {
                                System.out.print("Enter snippet ID to edit: ");
                                int snippetId = getValidIntInput(scanner, "Snippet ID");

                                System.out.print("New title: ");
                                String newTitle = getNonEmptyInput(scanner, "Title");

                                System.out.print("New language: ");
                                String newLanguage = getNonEmptyInput(scanner, "Language");

                                System.out.print("New category: ");
                                String newCategory = getNonEmptyInput(scanner, "Category");

                                System.out.print("New description: ");
                                String newDescription = getNonEmptyInput(scanner, "Description");

                                System.out.println("Enter new tags (comma-separated): ");
                                String tagsInput = scanner.nextLine();
                                String[] newTags = tagsInput.split(",");

                                System.out.println("Enter the new code (end with \"X\"): ");
                                StringBuilder newCodeBuilder = new StringBuilder();
                                while (true) {
                                    String line = scanner.nextLine();
                                    if ("X".equals(line)) break;
                                    newCodeBuilder.append(line).append(System.lineSeparator());
                                }
                                String newCode = newCodeBuilder.toString();
                                if (newCode.trim().isEmpty()) {
                                    throw new IllegalArgumentException("Code cannot be empty");
                                }
                                
                                manager.editSnippet(snippetId, newTitle, newLanguage, newCode);
                            } catch (IllegalArgumentException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        }
                        case 4 -> {
                            try {
                                System.out.print("Enter snippet ID to delete: ");
                                int snippetId = getValidIntInput(scanner, "Snippet ID");
                                manager.deleteSnippet(snippetId);
                            } catch (IllegalArgumentException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        }
                        case 5 -> {
                            System.out.println("""
                                Export options:
                                1. Export all snippets to a single file
                                2. Export by category
                                Select an option: """);
                            
                            if (scanner.hasNextInt()) {
                                int exportChoice = scanner.nextInt();
                                scanner.nextLine(); // Consume newline
                                
                                switch (exportChoice) {
                                    case 1 -> {
                                        System.out.print("Enter output filename: ");
                                        String filename = scanner.nextLine();
                                        try {
                                            SnippetExporter.exportToText(manager.getAllSnippets(), filename);
                                            System.out.println("Snippets exported successfully!");
                                        } catch (IOException e) {
                                            System.out.println("Error exporting snippets: " + e.getMessage());
                                        }
                                    }
                                    case 2 -> {
                                        System.out.print("Enter output directory: ");
                                        String directory = scanner.nextLine();
                                        try {
                                            SnippetExporter.exportByCategory(manager.getAllSnippets(), directory);
                                            System.out.println("Snippets exported by category successfully!");
                                        } catch (IOException e) {
                                            System.out.println("Error exporting snippets: " + e.getMessage());
                                        }
                                    }
                                    default -> System.out.println("Invalid export option.");
                                }
                            } else {
                                System.out.println("Please enter a valid number.");
                                scanner.nextLine(); // Consume invalid input
                            }
                        }
                        default -> System.out.println("Invalid option. Please try again.");
                    }
                } catch (Exception e) {
                    System.out.println("An error occurred: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("A critical error occurred: " + e.getMessage());
        } finally {
            System.out.println("Exiting the application.");
        }
    }

    private static String getNonEmptyInput(Scanner scanner, String fieldName) {
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
        return input;
    }

    private static int getValidIntInput(Scanner scanner, String fieldName) {
        if (!scanner.hasNextInt()) {
            scanner.nextLine(); // Consume invalid input
            throw new IllegalArgumentException("Please enter a valid " + fieldName);
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        if (value < 0) {
            throw new IllegalArgumentException(fieldName + " cannot be negative");
        }
        return value;
    }
}
