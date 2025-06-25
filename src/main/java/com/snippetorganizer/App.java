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
                        3. Edit snippet
                        4. Delete snippet
                        5. Export snippets
                        6. Analyze snippets
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
                                manager.listSnippets();
                                System.out.print("Enter snippet ID to edit: ");
                                int snippetId = getValidIntInput(scanner, "Snippet ID");

                                System.out.print("New title: ");
                                String newTitle = getNonEmptyInput(scanner, "Title");

                                System.out.print("New language: ");
                                String newLanguage = getNonEmptyInput(scanner, "Language");

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
                                manager.listSnippets();
                                System.out.print("Enter snippet ID to delete: ");
                                int snippetId = getValidIntInput(scanner, "Snippet ID");
                                manager.deleteSnippet(snippetId);
                            } catch (IllegalArgumentException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        }
                        case 5 -> {
                            System.out.print("Enter output filename: ");
                            String filename = scanner.nextLine();
                            try {
                                SnippetExporter.exportToText(manager.getAllSnippets(), filename);
                                System.out.println("Snippets exported successfully!");
                            } catch (IOException e) {
                                System.out.println("Error exporting snippets: " + e.getMessage());
                            }
                        }
                        case 6 -> {
                            // New analysis feature using SnippetComponent interface
                            System.out.println("\n=== SNIPPET ANALYSIS ===");
                            SnippetAnalyzer.displayAnalysis(manager.getSnippetComponent());
                            
                            // Show additional analysis options
                            System.out.print("\nWould you like to see snippets by language? (y/n): ");
                            String showByLanguage = scanner.nextLine();
                            if ("y".equalsIgnoreCase(showByLanguage)) {
                                System.out.print("Enter language to filter: ");
                                String language = scanner.nextLine();
                                var snippetsByLanguage = SnippetAnalyzer.findSnippetsByLanguage(manager.getSnippetComponent(), language);
                                if (snippetsByLanguage.isEmpty()) {
                                    System.out.println("No snippets found for language: " + language);
                                } else {
                                    System.out.println("Found " + snippetsByLanguage.size() + " snippets in " + language + ":");
                                    snippetsByLanguage.forEach(snippet -> 
                                        System.out.println("  - " + snippet.getTitle()));
                                }
                            }
                            
                            System.out.print("\nWould you like to see long snippets (>100 chars)? (y/n): ");
                            String showLongSnippets = scanner.nextLine();
                            if ("y".equalsIgnoreCase(showLongSnippets)) {
                                var longSnippets = SnippetAnalyzer.getSnippetsWithCodeLongerThan(manager.getSnippetComponent(), 100);
                                if (longSnippets.isEmpty()) {
                                    System.out.println("No snippets found with code longer than 100 characters.");
                                } else {
                                    System.out.println("Found " + longSnippets.size() + " snippets with code > 100 chars:");
                                    longSnippets.forEach(snippet -> 
                                        System.out.println("  - " + snippet.getTitle() + " (" + snippet.getCode().length() + " chars)"));
                                }
                            }
                            
                            System.out.print("\nWould you like to export a summary report? (y/n): ");
                            String exportReport = scanner.nextLine();
                            if ("y".equalsIgnoreCase(exportReport)) {
                                System.out.print("Enter report filename: ");
                                String reportFilename = scanner.nextLine();
                                try {
                                    SnippetExporter.exportSummaryReport(manager.getSnippetComponent(), reportFilename);
                                    System.out.println("Summary report exported successfully!");
                                } catch (IOException e) {
                                    System.out.println("Error exporting report: " + e.getMessage());
                                }
                            }
                            
                            System.out.print("\nWould you like to export by language? (y/n): ");
                            String exportByLanguage = scanner.nextLine();
                            if ("y".equalsIgnoreCase(exportByLanguage)) {
                                System.out.print("Enter base filename (e.g., 'snippets'): ");
                                String baseFilename = scanner.nextLine();
                                try {
                                    SnippetExporter.exportByLanguage(manager.getSnippetComponent(), baseFilename);
                                    System.out.println("Language-based export completed successfully!");
                                } catch (IOException e) {
                                    System.out.println("Error exporting by language: " + e.getMessage());
                                }
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
            scanner.nextLine();
            throw new IllegalArgumentException("Please enter a valid " + fieldName);
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        if (value < 0) {
            throw new IllegalArgumentException(fieldName + " cannot be negative");
        }
        return value;
    }
}
