package com.snippetorganizer;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/*
 * SnippetManager class manages the collection of code snippets.
 * It provides methods to add, search, edit, delete, and browse snippets.
 */
public class SnippetManager {
    private static final String FILE_NAME = "snippets.json";
    private final File file;
    private final ObjectMapper objectMapper;
    private final SnippetCollection snippetCollection;

    /*
     * Constructor for SnippetManager.
     * Initializes the file, object mapper, and snippet collection.
     */
    public SnippetManager() {
        this.file = new File(FILE_NAME);
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.snippetCollection = new SnippetCollection("Main Collection");
        loadSnippets();
    }

    /*
     * Loads snippets from the JSON file into the snippet collection.
     * If the file does not exist or is empty, it initializes an empty collection.
     */
    private void loadSnippets() {
        try {
            if (file.exists() && file.length() > 0) {
                Snippet[] snippets = objectMapper.readValue(file, Snippet[].class);
                for (Snippet snippet : snippets) {
                    snippetCollection.addSnippet(snippet);
                }
            }
        } catch (IOException e) {
            throw new SnippetException("Error loading snippets", e);
        }
    }

    /*
     * Generates the next ID for a new snippet.
     * It finds the maximum ID in the current collection and increments it by 1.
     * @return the next ID
     */
    private int getNextId() {
        int maxId = 0;
        SnippetIterator iterator = new SnippetIterator(snippetCollection.getSnippets());
        
        while (iterator.hasNext()) {
            Snippet snippet = iterator.next();
            if (snippet.getId() > maxId) {
                maxId = snippet.getId();
            }
        }
        return maxId + 1;
    }

    /*
     * Adds a new snippet to the collection.
     * @param title the title of the snippet
     * @param language the programming language of the snippet
     * @param code the code of the snippet
     */
    public void addSnippet(String title, String language, String code) {
        try {
            Snippet snippet = SnippetFactory.createSnippet(getNextId(),title,language,code);
            snippetCollection.addSnippet(snippet);
            saveSnippets();
            SnippetLogger.logInfo("Added new snippet: " + title);
        } catch (Exception e) {
            SnippetLogger.logError("Error adding snippet", e);
            throw new SnippetException("Error adding snippet", e);
        }
    }

    /*
     * Searches for snippets containing the specified keyword in title, code, or language.
     * @param keyword the keyword to search for
     */
    public void searchSnippets(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new SnippetException("Search keyword cannot be empty");
        }

        SnippetIterator iterator = new SnippetIterator(snippetCollection.getSnippets());
        boolean found = false;

        while (iterator.hasNext()) {
            Snippet snippet = iterator.next();
            if (snippet.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                snippet.getCode().toLowerCase().contains(keyword.toLowerCase()) ||
                snippet.getLanguage().toLowerCase().contains(keyword.toLowerCase())) {
                
                System.out.println("\nFound snippet:");
                System.out.println(snippet);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No snippets found containing: " + keyword);
        }
    }

    /*
     * Edits an existing snippet in the collection.
     * @param snippetId the ID of the snippet to edit
     * @param newTitle the new title of the snippet
     * @param newLanguage the new programming language of the snippet
     * @param newCategory the new category of the snippet
     */
    public void editSnippet(int snippetId, String newTitle, String newLanguage, String newCode) {
        SnippetIterator iterator = new SnippetIterator(snippetCollection.getSnippets());
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

    /*
     * Deletes a snippet from the collection.
     * @param snippetId the ID of the snippet to delete
     */
    public void deleteSnippet(int snippetId) {
        SnippetIterator iterator = new SnippetIterator(snippetCollection.getSnippets());
        boolean found = false;

        while (iterator.hasNext()) {
            Snippet snippet = iterator.next();
            if (snippet.getId() == snippetId) {
                snippetCollection.removeSnippet(snippet);
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

    /*
     * Retrieves all snippets in the collection.
     * @return a list of all snippets
     */
    public List<Snippet> getAllSnippets() {
        return snippetCollection.getSnippets();
    }

    /*
    *  Saves the current state of the snippet collection to the JSON file. 
    */
    private void saveSnippets() {
        try {
            objectMapper.writeValue(file, snippetCollection.getSnippets());
        } catch (IOException e) {
            SnippetLogger.logError("Error saving snippets", e);
            throw new SnippetException("Error saving snippets", e);
        }
    }
}
