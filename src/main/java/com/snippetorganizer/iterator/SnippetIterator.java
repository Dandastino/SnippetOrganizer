package com.snippetorganizer.iterator;

import java.util.Iterator;
import java.util.List;
import com.snippetorganizer.core.Snippet;

/**
 * Iterator implementation for traversing Snippet collections.
 * 
 * @author Sherif Moustafa
 * @version 1.0
 * @see Iterator
 * @see Snippet
 */
public class SnippetIterator implements Iterator<Snippet> {
    
    /** The list of snippets to iterate over */
    private final List<Snippet> snippets;
    
    /** The current position in the iteration */
    private int position;

    /**
     * Constructs a new SnippetIterator for the specified list of snippets.
     * 
     * @param snippets the list of snippets to iterate over (must not be null)
     * @throws IllegalArgumentException if the snippets list is null
     */
    public SnippetIterator(List<Snippet> snippets) {
        if (snippets == null) {
            throw new IllegalArgumentException("Snippets list cannot be null");
        }
        this.snippets = snippets;
        this.position = 0;
    }

    /**
     * Checks if there are more snippets to iterate over.
     * 
     * @return true if there are more snippets to iterate over, false otherwise
     */
    @Override
    public boolean hasNext() {
        return position < snippets.size();
    }

    /**
     * Retrieves the next snippet in the iteration.
     * 
     * @return the next Snippet object, or null if there are no more snippets
     */
    @Override
    public Snippet next() {
        if (hasNext()) {
            return snippets.get(position++);
        }
        return null;
    }
} 