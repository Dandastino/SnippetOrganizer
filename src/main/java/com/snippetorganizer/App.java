package com.snippetorganizer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Main application class for the Snippet Organizer.
 * 
 * @author Sherif Moustafa
 * @version 1.0
 */
public class App {
    
    /**
     * Main entry point for the Snippet Organizer application.
     * 
     * @param args command line arguments (not used in this application)
     */
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
                    7. Manage tags
                    0. Close application
                Select an option: """);
                    
                try {
                    choice = getValidMenuChoice(scanner);

                    switch (choice) {
                        case 0 -> handleExit();
                        case 1 -> handleAddSnippet(scanner, manager);
                        case 2 -> handleSearchSnippets(scanner, manager);
                        case 3 -> handleEditSnippet(scanner, manager);
                        case 4 -> handleDeleteSnippet(scanner, manager);
                        case 5 -> handleExportSnippets(scanner, manager);
                        case 6 -> handleAnalyzeSnippets(scanner, manager);
                        case 7 -> handleManageTags(scanner, manager);
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

    /**
     * Gets and validates the user's menu choice.
     * 
     * @param scanner the Scanner object for reading user input
     * @return the validated menu choice as an integer
     */
    private static int getValidMenuChoice(Scanner scanner) {
        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();
            scanner.nextLine();
            return choice;
        } else {
            System.out.println("Please insert a valid number.");
            scanner.next(); 
            return -1; 
        }
    }

    /** Handles the application exit. */
    private static void handleExit() {
        
    }

    /**
     * Handles adding a new snippet to the collection.
     * 
     * @param scanner the Scanner object for reading user input
     * @param manager the SnippetManager instance for adding snippets
     */
    private static void handleAddSnippet(Scanner scanner, SnippetManager manager) {
        try {
            System.out.print("Title: ");
            String title = getNonEmptyInput(scanner, "Title");

            System.out.print("Language: ");
            String language = getNonEmptyInput(scanner, "Language");

            System.out.print("Description (optional): ");
            String description = scanner.nextLine().trim();

            System.out.print("Tags (comma-separated, optional): ");
            String tagsInput = scanner.nextLine().trim();
            Set<String> tags = parseTags(tagsInput);

            System.out.println("Enter the code (end with \"X\"): ");
            String code = getCodeInput(scanner);
            
            manager.addSnippet(title, language, code, tags, description);
            System.out.println("Snippet added successfully!");
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handles searching for snippets in the collection.
     * 
     * @param scanner the Scanner object for reading user input
     * @param manager the SnippetManager instance for searching snippets
     */
    private static void handleSearchSnippets(Scanner scanner, SnippetManager manager) {
        System.out.println("Search options:");
        System.out.println("1. Search by keyword");
        System.out.println("2. Search by tag");
        System.out.print("Select search type: ");
        
        try {
            int searchChoice = getValidIntInput(scanner, "Search choice");
            switch (searchChoice) {
                case 1 -> {
                    System.out.print("Enter search keyword: ");
                    String keyword = scanner.nextLine();
                    manager.searchSnippets(keyword);
                }
                case 2 -> {
                    System.out.print("Enter tag to search: ");
                    String tag = scanner.nextLine();
                    manager.searchByTag(tag);
                }
                default -> System.out.println("Invalid search option.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handles editing an existing snippet.
     * 
     * @param scanner the Scanner object for reading user input
     * @param manager the SnippetManager instance for editing snippets
     */
    private static void handleEditSnippet(Scanner scanner, SnippetManager manager) {
        try {
            manager.listSnippets();
            System.out.print("Enter snippet ID to edit: ");
            int snippetId = getValidIntInput(scanner, "Snippet ID");

            System.out.print("New title: ");
            String newTitle = getNonEmptyInput(scanner, "Title");

            System.out.print("New language: ");
            String newLanguage = getNonEmptyInput(scanner, "Language");

            System.out.println("Enter the new code (end with \"X\"): ");
            String newCode = getCodeInput(scanner);
            
            manager.editSnippet(snippetId, newTitle, newLanguage, newCode);
            System.out.println("Snippet edited successfully!");
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handles deleting a snippet from the collection.
     * 
     * @param scanner the Scanner object for reading user input
     * @param manager the SnippetManager instance for deleting snippets
     */
    private static void handleDeleteSnippet(Scanner scanner, SnippetManager manager) {
        try {
            manager.listSnippets();
            System.out.print("Enter snippet ID to delete: ");
            int snippetId = getValidIntInput(scanner, "Snippet ID");
            manager.deleteSnippet(snippetId);
            System.out.println("Snippet deleted successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handles exporting snippets to a text file.
     * 
     * @param scanner the Scanner object for reading user input
     * @param manager the SnippetManager instance for accessing snippets
     */
    private static void handleExportSnippets(Scanner scanner, SnippetManager manager) {
        System.out.print("Enter output filename: ");
        String filename = scanner.nextLine();
        try {
            SnippetExporter.exportToText(manager.getAllSnippets(), filename);
            System.out.println("Snippets exported successfully!");
        } catch (IOException e) {
            System.out.println("Error exporting snippets: " + e.getMessage());
        }
    }

    /**
     * Handles snippet analysis and statistics.
     * 
     * @param scanner the Scanner object for reading user input
     * @param manager the SnippetManager instance for accessing snippets
     */
    private static void handleAnalyzeSnippets(Scanner scanner, SnippetManager manager) {
        System.out.println("\n=== SNIPPET ANALYSIS ===");
        SnippetAnalyzer.displayEnhancedAnalysis(manager.getSnippetComponent());
        
        offerLanguageAnalysis(scanner, manager);
        offerDescriptionAnalysis(scanner, manager);
        offerCodeLengthAnalysis(scanner, manager);
        offerExportOptions(scanner, manager);
    }

    /**
     * Handles tag management operations.
     * 
     * @param scanner the Scanner object for reading user input
     * @param manager the SnippetManager instance for managing tags
     */
    private static void handleManageTags(Scanner scanner, SnippetManager manager) {
        System.out.println("\n=== TAG MANAGEMENT ===");
        Set<String> allTags = manager.getAllTags();
        
        if (allTags.isEmpty()) {
            System.out.println("No tags found in your snippets.");
        } else {
            System.out.println("All tags in your collection:");
            allTags.forEach(tag -> System.out.println("  - " + tag));
        }
        
        System.out.println("\nTag management options:");
        System.out.println("1. Add tag to snippet");
        System.out.println("2. Remove tag from snippet");
        System.out.println("3. View snippets by tag");
        System.out.print("Select option (or press Enter to go back): ");
        
        String tagChoice = scanner.nextLine().trim();
        if (!tagChoice.isEmpty()) {
            try {
                int tagChoiceNum = Integer.parseInt(tagChoice);
                switch (tagChoiceNum) {
                    case 1 -> handleAddTagToSnippet(scanner, manager);
                    case 2 -> handleRemoveTagFromSnippet(scanner, manager);
                    case 3 -> handleViewSnippetsByTag(scanner, manager);
                    default -> System.out.println("Invalid option.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Handles adding a tag to an existing snippet.
     * 
     * @param scanner the Scanner object for reading user input
     * @param manager the SnippetManager instance for managing snippets
     */
    private static void handleAddTagToSnippet(Scanner scanner, SnippetManager manager) {
        manager.listSnippets();
        System.out.print("Enter snippet ID: ");
        // int snippetId = getValidIntInput(scanner, "Snippet ID");
        System.out.print("Enter tag to add: ");
        String tagToAdd = scanner.nextLine().trim();
        if (!tagToAdd.isEmpty()) {
            System.out.println("Tag management not fully implemented yet.");
        }
    }

    /**
     * Handles removing a tag from an existing snippet.
     * 
     * @param scanner the Scanner object for reading user input
     * @param manager the SnippetManager instance for managing snippets
     */
    private static void handleRemoveTagFromSnippet(Scanner scanner, SnippetManager manager) {
        manager.listSnippets();
        System.out.print("Enter snippet ID: ");
        // int snippetId = getValidIntInput(scanner, "Snippet ID");
        System.out.print("Enter tag to remove: ");
        String tagToRemove = scanner.nextLine().trim();
        if (!tagToRemove.isEmpty()) {
            System.out.println("Tag management not fully implemented yet.");
        }
    }

    /**
     * Handles viewing snippets by a specific tag.
     * 
     * @param scanner the Scanner object for reading user input
     * @param manager the SnippetManager instance for searching snippets
     */
    private static void handleViewSnippetsByTag(Scanner scanner, SnippetManager manager) {
        System.out.print("Enter tag to view snippets: ");
        String tagToView = scanner.nextLine().trim();
        if (!tagToView.isEmpty()) {
            manager.searchByTag(tagToView);
        }
    }

    /**
     * Offers additional language-based analysis options.
     * 
     * @param scanner the Scanner object for reading user input
     * @param manager the SnippetManager instance for accessing snippets
     */
    private static void offerLanguageAnalysis(Scanner scanner, SnippetManager manager) {
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
    }

    /**
     * Offers description-based analysis options.
     * 
     * @param scanner the Scanner object for reading user input
     * @param manager the SnippetManager instance for accessing snippets
     */
    private static void offerDescriptionAnalysis(Scanner scanner, SnippetManager manager) {
        System.out.print("\nWould you like to see snippets with descriptions? (y/n): ");
        String showWithDescriptions = scanner.nextLine();
        if ("y".equalsIgnoreCase(showWithDescriptions)) {
            var snippetsWithDescriptions = SnippetAnalyzer.getSnippetsWithDescriptions(manager.getSnippetComponent());
            if (snippetsWithDescriptions.isEmpty()) {
                System.out.println("No snippets found with descriptions.");
            } else {
                System.out.println("Found " + snippetsWithDescriptions.size() + " snippets with descriptions:");
                snippetsWithDescriptions.forEach(snippet -> 
                    System.out.println("  - " + snippet.getTitle() + ": " + snippet.getDescription()));
            }
        }
    }

    /**
     * Offers code length analysis options.
     * 
     * @param scanner the Scanner object for reading user input
     * @param manager the SnippetManager instance for accessing snippets
     */
    private static void offerCodeLengthAnalysis(Scanner scanner, SnippetManager manager) {
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
    }

    /**
     * Offers export options for analysis results.
     * 
     * @param scanner the Scanner object for reading user input
     * @param manager the SnippetManager instance for accessing snippets
     */
    private static void offerExportOptions(Scanner scanner, SnippetManager manager) {
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
        
        // Offer language-based export
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

    /**
     * Parses comma-separated tags from user input.
     * 
     * @param tagsInput the comma-separated string of tags
     * @return a Set containing the individual tags
     */
    private static Set<String> parseTags(String tagsInput) {
        Set<String> tags = new HashSet<>();
        if (!tagsInput.isEmpty()) {
            for (String tag : tagsInput.split(",")) {
                String trimmedTag = tag.trim();
                if (!trimmedTag.isEmpty()) {
                    tags.add(trimmedTag.toLowerCase());
                }
            }
        }
        return tags;
    }

    /**
     * Gets code input from the user until they enter "X".
     * 
     * @param scanner the Scanner object for reading user input
     * @return the complete code string
     * @throws IllegalArgumentException if the code is empty
     */
    private static String getCodeInput(Scanner scanner) {
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
        return code;
    }

    /**
     * Gets non-empty input from the user for required fields.
     * 
     * @param scanner the Scanner object for reading user input
     * @param fieldName the name of the field being requested (for error messages)
     * @return the validated non-empty input string
     * @throws IllegalArgumentException if the input is empty
     */
    private static String getNonEmptyInput(Scanner scanner, String fieldName) {
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
        return input;
    }

    /**
     * Gets and validates integer input from the user.
     * 
     * @param scanner the Scanner object for reading user input
     * @param fieldName the name of the field being requested (for error messages)
     * @return the validated integer input
     * @throws IllegalArgumentException if the input is not a valid integer or is negative
     */
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
