package com.snippetorganizer;

import java.util.Iterator;
import java.util.List;

/*
 * SnippetIterator class implements the Iterator interface for iterating over a list of Snippet objects.
 * It provides methods to check if there are more snippets and to retrieve the next snippet.
 */
public class SnippetIterator implements Iterator<Snippet> {
    private final List<Snippet> snippets;
    private int position;

    /*
     * Constructor for SnippetIterator.
     * @param snippets the list of snippets to iterate over
     */
    public SnippetIterator(List<Snippet> snippets) {
        this.snippets = snippets;
        this.position = 0;
    }

    /*
     * Checks if there are more snippets to iterate over.
     * @return true if there are more snippets, false otherwise
     */
    @Override
    public boolean hasNext() {
        return position < snippets.size();
    }

    /*
     * Retrieves the next snippet in the iteration.
     * @return the next Snippet object
     */
    @Override
    public Snippet next() {
        if (hasNext()) {
            return snippets.get(position++);
        }
        return null;
    }
} 