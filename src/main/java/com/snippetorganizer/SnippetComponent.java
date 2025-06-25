package com.snippetorganizer;

import java.util.List;

/**
 * SnippetComponent interface for the Composite Pattern.
 * This allows treating individual snippets and collections uniformly.
 */
public interface SnippetComponent {
    
    /**
     * Get the name/title of this component
     * @return the name of the component
     */
    String getName();
    
    /**
     * Get all snippets contained in this component
     * @return list of all snippets
     */
    List<Snippet> getAllSnippets();
    
    /**
     * Add a snippet to this component
     * @param snippet the snippet to add
     */
    void addSnippet(Snippet snippet);
    
    /**
     * Remove a snippet from this component
     * @param snippet the snippet to remove
     */
    void removeSnippet(Snippet snippet);
    
    /**
     * Get the total number of snippets in this component
     * @return the number of snippets
     */
    int getSnippetCount();
    
    /**
     * Check if this component is empty
     * @return true if empty, false otherwise
     */
    boolean isEmpty();
    
    /**
     * Display information about this component
     */
    void display();
} 