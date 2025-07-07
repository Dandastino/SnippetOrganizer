package com.snippetorganizer.core;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.snippetorganizer.composite.SnippetComponent;
import com.snippetorganizer.composite.SnippetCollection;
import com.snippetorganizer.exception.SnippetException;
import com.snippetorganizer.factory.SnippetFactory;
import com.snippetorganizer.iterator.SnippetIterator;
import com.snippetorganizer.logging.SnippetLogger;

/**
 * Core management class for the Snippet Organizer application.
 * 
 * @author Sherif Moustafa
 * @version 1.0
 * @see SnippetComponent
 * @see Snippet
 * @see SnippetCollection
 */
public class SnippetManager {


    
    /** The filename for storing snippets in JSON format */
    private static final String FILE_NAME = "snippets.json";
    
    /** The data directory for storing application files */
    private static final String DATA_DIR = "data";
    
    /** The file object for snippet persistence */
    private final File file;
    
    /** Jackson ObjectMapper for JSON serialization/deserialization */
    private final ObjectMapper objectMapper;
    
    /** The main snippet component (collection) for organizing snippets */
    private final SnippetComponent snippetComponent;

    /**
     * Constructs a new SnippetManager and initializes the system.
     */
    public SnippetManager() {
        this(new File(DATA_DIR, FILE_NAME).getPath());
    }

    /**
     * Constructs a new SnippetManager with a custom data file path (for testing).
     * @param dataFilePath the path to the data file to use
     */
    public SnippetManager(String dataFilePath) {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        this.file = new File(dataFilePath);
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.snippetComponent = new SnippetCollection("Main Collection");

        try {
            // Create the file only if it does not exist
            if (!file.exists()) {
                file.createNewFile();
                objectMapper.writeValue(file, new Snippet[0]);
            }

            // Create the log file only if it does not exist
            File logFile = new File(dataDir, "snippet_organizer.log");
            if (!logFile.exists()) {
                logFile.createNewFile();
            }

            System.out.println("Files checked on startup.");
        } catch (IOException e) {
            SnippetLogger.logError("Error initializing files", e);
            throw SnippetException.ioError("Failed to initialize application files", e);
        }

        loadSnippets();
    }

    /** Displays all snippets in the collection. */ 
    public void listSnippets() {
        if (snippetComponent.isEmpty()) {
            System.out.println("No snippets available.");
            return;
        }

        snippetComponent.display();
    }

    /** Loads snippets from the JSON file into the snippet component. */
    private void loadSnippets() {
        if (file.exists() && file.length() > 0) {
            try {
                Snippet[] snippets = objectMapper.readValue(file, Snippet[].class);
                for (Snippet snippet : snippets) {
                    snippetComponent.addSnippet(snippet);
                }
                System.out.println("Successfully loaded " + snippets.length + " snippets from file.");
            } catch (IOException e) {
                SnippetLogger.logError("Error reading snippets file", e);
                throw SnippetException.ioError("Failed to load snippets from file", e);
            }
        }
    }

    /**
     * Generates the next available ID for a new snippet.
     * 
     * @return the next available ID for a new snippet
     */
    private int getNextId() {
        int maxId = 0;
        for (Snippet snippet : snippetComponent.getAllSnippets()) {
            if (snippet.getId() > maxId) {
                maxId = snippet.getId();
            }
        }
        return maxId + 1;
    }

    /**
     * Adds a new snippet to the collection with complete information.
     *
     * @param title the title of the snippet (must not be null or empty)
     * @param language the programming language of the snippet (must not be null or empty)
     * @param code the code content of the snippet (must not be null or empty)
     * @param tags the tags for categorization (can be null or empty)
     * @param description the description of the snippet (can be null or empty)
     * @throws SnippetException if an error occurs during snippet creation or persistence
     */
    public void addSnippet(String title, String language, String code, Set<String> tags, String description) {
        try {
            Snippet newSnippet = SnippetFactory.createSnippet(getNextId(), title, language, code, tags, description);
            snippetComponent.addSnippet(newSnippet);
            saveSnippets();
            SnippetLogger.logInfo("Added new snippet: " + title);
        } catch (SnippetException e) {
            SnippetLogger.logError("Error adding snippet", e);
            throw e; // Re-throw SnippetException as-is
        } catch (Exception e) {
            SnippetLogger.logError("Unexpected error adding snippet", e);
            throw SnippetException.systemError("Unexpected error occurred while adding snippet", e);
        }
    }

    /**
     * Adds a new snippet to the collection with incomplete information.
     *
     * @param title the title of the snippet (must not be null or empty)
     * @param language the programming language of the snippet (must not be null or empty)
     * @param code the code content of the snippet (must not be null or empty)
     * @throws SnippetException if an error occurs during snippet creation or persistence
     */
    public void addSnippet(String title, String language, String code) {
        addSnippet(title, language, code, new HashSet<>(), "");
    }

    /**
     * Searches for snippets containing the specified keyword.
     * 
     * @param keyword the keyword to search for (must not be null or empty)
     * @throws SnippetException if the keyword is invalid
     */
    public void searchSnippets(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw SnippetException.validationError("Please provide a valid value for searching.");
        }

        boolean found = false;
        String lowerKeyword = keyword.toLowerCase();
        SnippetIterator iterator = new SnippetIterator(snippetComponent.getAllSnippets());

        while (iterator.hasNext()) {
            Snippet snippet = iterator.next();
            if (snippet.getTitle().toLowerCase().contains(lowerKeyword) ||
                snippet.getCode().toLowerCase().contains(lowerKeyword) ||
                snippet.getLanguage().toLowerCase().contains(lowerKeyword) ||
                snippet.getDescription().toLowerCase().contains(lowerKeyword) ||
                snippet.getTags().stream().anyMatch(tag -> tag.contains(lowerKeyword))) {
                
                System.out.println("\nFound snippet:");
                System.out.println("ID: " + snippet.getId());
                System.out.println("Title: " + snippet.getTitle());
                System.out.println("Language: " + snippet.getLanguage());
                if (!snippet.getDescription().isEmpty()) {
                    System.out.println("Description: " + snippet.getDescription());
                }
                if (!snippet.getTags().isEmpty()) {
                    System.out.println("Tags: " + String.join(", ", snippet.getTags()));
                }
                System.out.println("Code:\n" + snippet.getCode());
                System.out.println("---------------------------");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No snippets found containing: " + keyword);
        }
    }

    /**
     * Searches for snippets by a specific tag.
     * 
     * @param tag the tag to search for (must not be null or empty)
     * @throws SnippetException if the tag is invalid
     */
    public void searchByTag(String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            throw SnippetException.validationError("Please provide a valid tag for searching.");
        }

        SnippetIterator iterator = new SnippetIterator(snippetComponent.getAllSnippets());
        boolean found = false;
        String lowerTag = tag.toLowerCase();

        while (iterator.hasNext()) {
            Snippet snippet = iterator.next();
            if (snippet.hasTag(lowerTag)) {
                System.out.println("\nFound snippet with tag '" + tag + "':");
                System.out.println("ID: " + snippet.getId());
                System.out.println("Title: " + snippet.getTitle());
                System.out.println("Language: " + snippet.getLanguage());
                System.out.println("Tags: " + String.join(", ", snippet.getTags()));
                System.out.println("Code:\n" + snippet.getCode());
                System.out.println("---------------------------");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No snippets found with tag: " + tag);
        }
    }

    /**
     * Gets all unique tags used in the collection.
     * 
     * @return a set containing all unique tags used in the collection
     */
    public Set<String> getAllTags() {
        Set<String> allTags = new HashSet<>();
        SnippetIterator iterator = new SnippetIterator(snippetComponent.getAllSnippets());
        
        while (iterator.hasNext()) {
            Snippet snippet = iterator.next();
            allTags.addAll(snippet.getTags());
        }
        
        return allTags;
    }

    /**
     * Edits an existing snippet in the collection.
     * 
     * @param snippetId the ID of the snippet to edit (must be non-negative)
     * @param newTitle the new title for the snippet (must not be null or empty)
     * @param newLanguage the new programming language (must not be null or empty)
     * @param newCode the new code content (must not be null or empty)
     * @throws SnippetException if the snippet is not found or an error occurs during editing
     */
    public void editSnippet(int snippetId, String newTitle, String newLanguage, String newCode) {
        SnippetIterator iterator = new SnippetIterator(snippetComponent.getAllSnippets());
        boolean found = false;

        while (iterator.hasNext()) {
            Snippet snippet = iterator.next();
            if (snippet.getId() == snippetId) {
                snippet.setTitle(newTitle);
                snippet.setLanguage(newLanguage);
                snippet.setCode(newCode);
                found = true;
                break;
            }
        }

        if (found) {
            try {
                saveSnippets();
                SnippetLogger.logInfo("Edited snippet with ID: " + snippetId);
            } catch (SnippetException e) {
                SnippetLogger.logError("Error saving after edit", e);
                throw e;
            }
        } else {
            throw SnippetException.notFound("No snippet found with ID: " + snippetId);
        }
    }

    /**
     * Deletes a snippet from the collection.
     * 
     * @param snippetId the ID of the snippet to delete (must be non-negative)
     * @throws SnippetException if the snippet is not found or an error occurs during deletion
     */
    public void deleteSnippet(int snippetId) {
        SnippetIterator iterator = new SnippetIterator(snippetComponent.getAllSnippets());
        boolean found = false;

        while (iterator.hasNext()) {
            Snippet snippet = iterator.next();
            if (snippet.getId() == snippetId) {
                snippetComponent.removeSnippet(snippet);
                found = true;
                break;
            }
        }

        if (found) {
            try {
                saveSnippets();
                SnippetLogger.logInfo("Deleted snippet with ID: " + snippetId);
            } catch (SnippetException e) {
                SnippetLogger.logError("Error saving after deletion", e);
                throw e;
            }
        } else {
            throw SnippetException.notFound("No snippet found with ID: " + snippetId);
        }
    }

    /**
     * Retrieves all snippets in the collection.
     * 
     * @return a list containing all snippets in the collection
     */
    public List<Snippet> getAllSnippets() {
        return snippetComponent.getAllSnippets();
    }

    /**
     * Gets the snippet component for advanced operations.
     * 
     * @return the snippet component
     */
    public SnippetComponent getSnippetComponent() {
        return snippetComponent;
    }

    /**
     * Gets the total number of snippets in the collection.
     * 
     * @return the number of snippets in the collection
     */
    public int getSnippetCount() {
        return snippetComponent.getSnippetCount();
    }

    /**
     * Demonstrates the Composite pattern by creating a nested collection structure.
     * This method shows how the Composite pattern allows treating individual snippets
     * and collections of snippets uniformly.
     * 
     * @return a composite structure containing multiple snippet collections
     */
    public SnippetComponent createCompositeDemo() {
        // Create the main collection
        SnippetCollection mainCollection = new SnippetCollection("Main Collection");
        
        // Create sub-collections using the Composite pattern
        SnippetCollection javaCollection = new SnippetCollection("Java Snippets");
        SnippetCollection pythonCollection = new SnippetCollection("Python Snippets");
        SnippetCollection webCollection = new SnippetCollection("Web Development");
        
        // Use the Factory pattern to create snippets for each collection
        try {
            // Add snippets to Java collection
            javaCollection.addSnippet(SnippetFactory.createSnippetWithTags(1, "Java Class", "Java", 
                "public class Example {\n    private String name;\n    public Example(String name) {\n        this.name = name;\n    }\n}", 
                Set.of("class", "constructor", "java")));
            
            javaCollection.addSnippet(SnippetFactory.createSnippetWithDescription(2, "Java Method", "Java", 
                "public void processData(String data) {\n    if (data != null) {\n        System.out.println(data);\n    }\n}", 
                "A method that processes string data safely"));
            
            // Add snippets to Python collection
            pythonCollection.addSnippet(SnippetFactory.createSnippet(3, "Python Function", "Python", 
                "def calculate_sum(a, b):\n    return a + b"));
            
            pythonCollection.addSnippet(SnippetFactory.createSnippetWithTags(4, "Python List Comprehension", "Python", 
                "squares = [x**2 for x in range(10)]", 
                Set.of("list", "comprehension", "python")));
            
            // Add snippets to Web collection
            webCollection.addSnippet(SnippetFactory.createSnippetWithTags(5, "HTML Structure", "HTML", 
                "<!DOCTYPE html>\n<html>\n<head>\n    <title>Page</title>\n</head>\n<body>\n    <h1>Hello World</h1>\n</body>\n</html>", 
                Set.of("html", "structure", "web")));
            
            webCollection.addSnippet(SnippetFactory.createSnippetWithDescription(6, "CSS Styling", "CSS", 
                ".container {\n    max-width: 1200px;\n    margin: 0 auto;\n    padding: 20px;\n}", 
                "Responsive container styling"));
            
            // Add sub-collections to main collection (Composite pattern)
            mainCollection.addSnippet(javaCollection);
            mainCollection.addSnippet(pythonCollection);
            mainCollection.addSnippet(webCollection);
            
            SnippetLogger.logInfo("Created composite demo structure with " + mainCollection.getSnippetCount() + " total snippets");
            
        } catch (Exception e) {
            SnippetLogger.logError("Error creating composite demo", e);
        }
        
        return mainCollection;
    }

    /**
     * Saves the current state of the snippet collection to the JSON file.
     *
     * @throws SnippetException if an error occurs during file writing
     */
    private void saveSnippets() {
        try {
            List<Snippet> allSnippets = snippetComponent.getAllSnippets();
            objectMapper.writeValue(file, allSnippets);
        } catch (IOException e) {
            SnippetLogger.logError("Error saving snippets", e);
            throw SnippetException.ioError("Failed to save snippets to file", e);
        }
    }
}
