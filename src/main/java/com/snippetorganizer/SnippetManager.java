package com.snippetorganizer;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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

    /** Constructs a new SnippetManager and initializes the system. */
    public SnippetManager() {
        // I have to change here and make it substitute the fie an not delate it and create a new one
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        
        this.file = new File(dataDir, FILE_NAME);
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.snippetComponent = new SnippetCollection("Main Collection");
        
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            objectMapper.writeValue(file, new Snippet[0]);
            
            File logFile = new File(dataDir, "snippet_organizer.log");
            if (logFile.exists()) {
                logFile.delete();
            }
            logFile.createNewFile();
            
            System.out.println("Files cleared on startup.");
        } catch (IOException e) {
            System.err.println("Warning: Error clearing files: " + e.getMessage());
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
                // Clear existing snippets and add loaded ones
                for (Snippet snippet : snippets) {
                    snippetComponent.addSnippet(snippet);
                }
                System.out.println("Successfully loaded " + snippets.length + " snippets from file.");
            } catch (IOException e) {
                System.err.println("Warning: Error reading snippets file: " + e.getMessage());
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
     * Adds a new snippet to the collection with basic information.
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
            Snippet newSnippet = new Snippet(getNextId(), title, language, code, tags, description);
            snippetComponent.addSnippet(newSnippet);
            saveSnippets();
            
            SnippetLogger.logInfo("Added new snippet: " + title);
        } catch (Exception e) {
            SnippetLogger.logError("Error adding snippet", e);
            throw new SnippetException("Error adding snippet", e);
        }
    }

    /**
     * Searches for snippets containing the specified keyword.
     * 
     * @param keyword the keyword to search for (must not be null or empty)
     * @throws SnippetException if the keyword is invalid
     */
    public void searchSnippets(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new SnippetException("Please provide a valid value for searching.");
        }

        SnippetIterator iterator = new SnippetIterator(snippetComponent.getAllSnippets());
        boolean found = false;
        String lowerKeyword = keyword.toLowerCase();

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
            throw new SnippetException("Please provide a valid tag for searching.");
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
            saveSnippets();
            SnippetLogger.logInfo("Edited snippet with ID: " + snippetId);
        } else {
            throw new SnippetException("No snippet found with ID: " + snippetId);
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
            saveSnippets();
            SnippetLogger.logInfo("Deleted snippet with ID: " + snippetId);
        } else {
            throw new SnippetException("No snippet found with ID: " + snippetId);
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
            throw new SnippetException("Error saving snippets", e);
        }
    }
}
