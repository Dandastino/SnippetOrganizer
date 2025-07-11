package com.snippetorganizer.factory;

import java.util.Set;
import com.snippetorganizer.core.Snippet;
import com.snippetorganizer.exception.SnippetException;

/**
 * Factory class for creating Snippet objects. (Creational Pattern)
 * Implements the Factory pattern to centralize snippet creation logic.
 * 
 * @author Sherif Moustafa
 * @version 1.0
 * @see Snippet
 */
public class SnippetFactory {
    
    /**
     * Creates a new Snippet with basic information.
     * 
     * @param id the unique identifier for the snippet (must be non-negative)
     * @param title the title of the snippet (must not be null or empty)
     * @param language the programming language of the snippet (must not be null or empty)
     * @param code the actual code content (must not be null or empty)
     * @return a new Snippet instance with the specified parameters
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public static Snippet createSnippet(int id, String title, String language, String code) {
        validateBasicParameters(id, title, language, code);
        return new Snippet(id, title, language, code);
    }
    
    /**
     * Creates a new Snippet with complete information including tags and description.
     * 
     * @param id the unique identifier for the snippet (must be non-negative)
     * @param title the title of the snippet (must not be null or empty)
     * @param language the programming language of the snippet (must not be null or empty)
     * @param code the actual code content (must not be null or empty)
     * @param tags the tags for categorization (can be null or empty)
     * @param description the description of the snippet (can be null or empty)
     * @return a new Snippet instance with the specified parameters
     * @throws IllegalArgumentException if required parameters are invalid
     */
    public static Snippet createSnippet(int id, String title, String language, String code, Set<String> tags, String description) {
        validateBasicParameters(id, title, language, code);
        return new Snippet(id, title, language, code, tags, description);
    }
    
    /**
     * Creates a new Snippet with tags but no description.
     * 
     * @param id the unique identifier for the snippet (must be non-negative)
     * @param title the title of the snippet (must not be null or empty)
     * @param language the programming language of the snippet (must not be null or empty)
     * @param code the actual code content (must not be null or empty)
     * @param tags the tags for categorization (can be null or empty)
     * @return a new Snippet instance with the specified parameters
     * @throws IllegalArgumentException if any required parameter is invalid
     */
    public static Snippet createSnippetWithTags(int id, String title, String language, String code, Set<String> tags) {
        return createSnippet(id, title, language, code, tags, null);
    }
    
    /**
     * Creates a new Snippet with description but no tags.
     * 
     * @param id the unique identifier for the snippet (must be non-negative)
     * @param title the title of the snippet (must not be null or empty)
     * @param language the programming language of the snippet (must not be null or empty)
     * @param code the actual code content (must not be null or empty)
     * @param description the description of the snippet (can be null or empty)
     * @return a new Snippet instance with the specified parameters
     * @throws IllegalArgumentException if any required parameter is invalid
     */
    public static Snippet createSnippetWithDescription(int id, String title, String language, String code, String description) {
        return createSnippet(id, title, language, code, null, description);
    }
    
    /**
     * Validates the basic parameters required for snippet creation.
     * 
     * @param id the unique identifier for the snippet
     * @param title the title of the snippet
     * @param language the programming language of the snippet
     * @param code the actual code content
     * @throws SnippetException if any parameter is invalid
     */
    private static void validateBasicParameters(int id, String title, String language, String code) {
        if (id < 0) {
            throw SnippetException.validationError("ID cannot be negative");
        }
        if (title == null || title.trim().isEmpty()) {
            throw SnippetException.validationError("Title cannot be null or empty");
        }
        if (language == null || language.trim().isEmpty()) {
            throw SnippetException.validationError("Language cannot be null or empty");
        }
        if (code == null || code.trim().isEmpty()) {
            throw SnippetException.validationError("Code cannot be null or empty");
        }
    }
}