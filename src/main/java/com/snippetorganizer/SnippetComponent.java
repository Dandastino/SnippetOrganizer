package com.snippetorganizer;

import java.util.List;

/**
 * SnippetComponent interface for the Composite Pattern implementation.
 * @author Sherif Moustafa
 * @version 1.0
 * @see Snippet
 * @see SnippetCollection
 */
public interface SnippetComponent {
    
    /**
     * Gets the name or title of this component.
     * 
     * @return the name of the component
     */
    String getName();
    
    /**
     * Gets all snippets contained in this component.
     * 
     * @return list of all snippets in this component
     */
    List<Snippet> getAllSnippets();
    
    /**
     * Adds a snippet to this component.
     * 
     * @param snippet the snippet to add
     * @throws UnsupportedOperationException if the component doesn't support adding snippets
     */
    void addSnippet(Snippet snippet);
    
    /**
     * Removes a snippet from this component.
     * 
     * @param snippet the snippet to remove
     * @throws UnsupportedOperationException if the component doesn't support removing snippets
     */
    void removeSnippet(Snippet snippet);
    
    /**
     * Gets the total number of snippets in this component.
     * 
     * @return the number of snippets in this component
     */
    int getSnippetCount();
    
    /**
     * Checks if this component is empty.
     * 
     * @return true if the component is empty, false otherwise
     */
    boolean isEmpty();
    
    /**
     * Displays information about this component.
     */
    void display();
} 