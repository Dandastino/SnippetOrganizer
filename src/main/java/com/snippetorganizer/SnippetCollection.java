package com.snippetorganizer;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Collection class for managing groups of code snippets.
 * @author Sherif Moustafa
 * @version 1.0
 * @see SnippetComponent
 * @see Snippet
 */
public class SnippetCollection implements SnippetComponent {
    
    /** The list of snippets in this collection */
    private final List<Snippet> snippets;
    
    /** The name of this collection */
    private String name;

    /**
     * Constructs a new SnippetCollection with the specified name.
     * @param name the name of the collection (must not be null or empty)
     * @throws IllegalArgumentException if the name is null or empty
     */
    public SnippetCollection(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Collection name cannot be null or empty");
        }
        this.name = name;
        this.snippets = new ArrayList<>();
    }

    /**
     * Adds a snippet to this collection.
     * @param snippet the snippet to add (must not be null)
     * @throws IllegalArgumentException if the snippet is null
     */
    @Override
    public void addSnippet(Snippet snippet) {
        if (snippet == null) {
            throw new IllegalArgumentException("Snippet cannot be null");
        }
        snippets.add(snippet);
    }

    /**
     * Removes a snippet from this collection.
     * @param snippet the snippet to remove (must not be null)
     * @throws IllegalArgumentException if the snippet is null
     */
    @Override
    public void removeSnippet(Snippet snippet) {
        if (snippet == null) {
            throw new IllegalArgumentException("Snippet cannot be null");
        }
        snippets.remove(snippet);
    }

    /**
     * Returns a list of all snippets in this collection.
     * @return a new list containing all snippets in this collection
     */
    @Override
    public List<Snippet> getAllSnippets() {
        return new ArrayList<>(snippets);
    }

    /**
     * Returns the name of this collection.
     * @return the name of the collection
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this collection.
     * @param name the new name for the collection (must not be null or empty)
     * @throws IllegalArgumentException if the name is null or empty
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Collection name cannot be null or empty");
        }
        this.name = name;
    }

    /**
     * Returns the number of snippets in this collection.
     * @return the number of snippets in the collection
     */
    @Override
    public int getSnippetCount() {
        return snippets.size();
    }

    /**
     * Checks if this collection is empty.
     * @return true if the collection is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return snippets.isEmpty();
    }

    /**
     * Displays information about this collection and all its snippets.
     */
    @JsonIgnore
    @Override
    public void display() {
        System.out.println("Collection: " + name);
        System.out.println("Total snippets: " + getSnippetCount());
        System.out.println("---------------------------");
        
        if (snippets.isEmpty()) {
            System.out.println("No snippets in this collection.");
        } else {
            for (Snippet snippet : snippets) {
                snippet.display();
                System.out.println("---------------------------");
            }
        }
    }

    /**
     * Legacy method for backward compatibility.
     * @return a list of all snippets in this collection
     * @deprecated Use {@link #getAllSnippets()} instead
     */
    @Deprecated
    public List<Snippet> getSnippets() {
        return getAllSnippets();
    }

    /**
     * Legacy method for backward compatibility.backward compatibility with existing code.</p>
     * @return the number of snippets in this collection
     * @deprecated Use {@link #getSnippetCount()} instead
     */
    @Deprecated
    public int size() {
        return getSnippetCount();
    }
} 