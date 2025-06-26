package com.snippetorganizer;

/**
 * Factory class for creating Snippet objects.
 * 
 * @author Sherif Moustafa
 * @version 1.0
 * @see Snippet
 */
public class SnippetFactory {
    
    /**
     * Creates a new Snippet with the specified parameters.
     * 
     * @param id the unique identifier for the snippet (must be non-negative)
     * @param title the title of the snippet (must not be null or empty)
     * @param language the programming language of the snippet (must not be null or empty)
     * @param code the actual code content (must not be null or empty)
     * @return a new Snippet instance with the specified parameters
     * @throws IllegalArgumentException if any parameter is invalid
     * @see Snippet#Snippet(int, String, String, String)
     */
    public static Snippet createSnippet(int id, String title, String language, String code) {
        return new Snippet(id, title, language, code);
    }
}