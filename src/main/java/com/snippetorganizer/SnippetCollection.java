package com.snippetorganizer;

import java.util.ArrayList;
import java.util.List;

/*
 * SnippetCollection class represents a collection of code snippets.
 * It provides methods to add, remove, and retrieve snippets.
 */
public class SnippetCollection {
    private final List<Snippet> snippets;
    private String name;

    /*
     * Constructor for SnippetCollection.
     * @param name the name of the collection
     */
    public SnippetCollection(String name) {
        this.name = name;
        this.snippets = new ArrayList<>();
    }

    /*
     * Adds a snippet to the collection.
     * @param snippet the snippet to add
     */
    public void addSnippet(Snippet snippet) {
        snippets.add(snippet);
    }

    /*
     * Removes a snippet from the collection.
     * @param snippet the snippet to remove
     */
    public void removeSnippet(Snippet snippet) {
        snippets.remove(snippet);
    }

    /*
     * Returns a list of all snippets in the collection.
     * @return a list of snippets
     */
    public List<Snippet> getSnippets() {
        return new ArrayList<>(snippets);
    }

    /*
     * Returns the name of the collection.
     * @return the name of the collection
     */
    public String getName() {
        return name;
    }

    /*
     * Sets the name of the collection.
     * @param name the new name of the collection
     */
    public void setName(String name) {
        this.name = name;
    }

    /*
     * Returns the number of snippets in the collection.
     * @return the size of the collection
     */
    public int size() {
        return snippets.size();
    }

    /*
     * Checks if the collection is empty.
     * @return true if the collection is empty, false otherwise
     */
    public boolean isEmpty() {
        return snippets.isEmpty();
    }
} 