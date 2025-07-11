package com.snippetorganizer.composite;

import java.util.List;
import com.snippetorganizer.core.Snippet;

/**
 * SnippetComponent interface for the Composite Pattern implementation. (Structural Pattern)
 * Supports both individual snippets and collections of snippets.
 * 
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
     * Adds a snippet component to this component.
     * This method supports the Composite pattern by allowing collections to be added.
     * 
     * @param component the snippet component to add
     * @throws UnsupportedOperationException if the component doesn't support adding other components
     */
    default void addSnippet(SnippetComponent component) {
        throw new UnsupportedOperationException("This component does not support adding other components");
    }
    
    /**
     * Removes a snippet from this component.
     * 
     * @param snippet the snippet to remove
     * @throws UnsupportedOperationException if the component doesn't support removing snippets
     */
    void removeSnippet(Snippet snippet);
    
    /**
     * Removes a snippet component from this component.
     * This method supports the Composite pattern by allowing collections to be removed.
     * 
     * @param component the snippet component to remove
     * @throws UnsupportedOperationException if the component doesn't support removing other components
     */
    default void removeSnippet(SnippetComponent component) {
        throw new UnsupportedOperationException("This component does not support removing other components");
    }
    
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