package com.snippetorganizer;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

/*
 * Snippet class represents a code snippet with an ID, title, language, and code.
 * It provides methods to get and set these properties, as well as a method to return a string representation of the snippet.
 * Implements SnippetComponent as a leaf node in the Composite Pattern.
 */
public final class Snippet implements SnippetComponent {

    private int id;
    private String title;
    private String language;
    private String code;

    /*
     * Default constructor for Snippet.
     * Initializes the snippet with default values.
     * @param id the ID of the snippet
     * @param title the title of the snippet
     * @param language the programming language of the snippet
     * @param code the code of the snippet
     */
    public Snippet(int id, String title, String language, String code) {
        this.id = id;
        setTitle(title);
        setLanguage(language);
        setCode(code);
    }

    /*
     * Get the code of the snippet
     * @return the code of the snippet
     */
    public String getCode() {
        return code;
    }

    /*
     * Set the code of the snippet.
     * @param code the code to set
     */
    public void setCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Code cannot be null or empty");
        }
        this.code = code;
    }

    /*
     * Get the ID of the snippet.
     */
    public int getId() {
        return id;
    }

    /*
     * Set the ID of the snippet.
     * @param id the ID to set
     */
    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("ID cannot be negative");
        }
        this.id = id;
    }

    /*
     * Get the title of the snippet.
     * @return the title of the snippet
     */
    public String getTitle() {
        return title;
    } 

    /*
     * Set the title of the snippet.
     *  @param title the title to set
     */
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        this.title = title;
    }
    
    /*
     * Get the language of the snippet.
     */
    public String getLanguage() {
        return language;
    }

    /*
     * Set the language of the snippet.
     * @param language the language to set
     * @throws IllegalArgumentException if the language is null or empty
     */
    public void setLanguage(String language) {
        if (language == null || language.trim().isEmpty()) {
            throw new IllegalArgumentException("Language cannot be null or empty");
        }
        this.language = language;
    }

    // Composite Pattern Implementation
    @JsonIgnore
    @Override
    public String getName() {
        return title;
    }

    @JsonIgnore
    @Override
    public List<Snippet> getAllSnippets() {
        return List.of(this);
    }

    @JsonIgnore
    @Override
    public void addSnippet(Snippet snippet) {
        throw new UnsupportedOperationException("Cannot add snippet to individual snippet");
    }

    @JsonIgnore
    @Override
    public void removeSnippet(Snippet snippet) {
        throw new UnsupportedOperationException("Cannot remove snippet from individual snippet");
    }

    @JsonIgnore
    @Override
    public int getSnippetCount() {
        return 1;
    }

    @JsonIgnore
    @Override
    public boolean isEmpty() {
        return false;
    }

    @JsonIgnore
    @Override
    public void display() {
        System.out.println(this.toString());
    }

    /*
     * Override the toString method to provide a string representation of the Snippet object.
     * @return a string representation of the Snippet object
     */
    @Override
    public String toString() {
        return String.format("""
            Snippet ID: %d
            Title: %s
            Language: %s
            Code:
            %s""", id, title, language, code);
    }
}