package com.snippetorganizer;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

/*
 * SnippetCollection class represents a collection of code snippets.
 * It provides methods to add, remove, and retrieve snippets.
 * Implements SnippetComponent as a composite node in the Composite Pattern.
 */
public class SnippetCollection implements SnippetComponent {
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
    @Override
    public void addSnippet(Snippet snippet) {
        snippets.add(snippet);
    }

    /*
     * Removes a snippet from the collection.
     * @param snippet the snippet to remove
     */
    @Override
    public void removeSnippet(Snippet snippet) {
        snippets.remove(snippet);
    }

    /*
     * Returns a list of all snippets in the collection.
     * @return a list of snippets
     */
    @Override
    public List<Snippet> getAllSnippets() {
        return new ArrayList<>(snippets);
    }

    /*
     * Returns the name of the collection.
     * @return the name of the collection
     */
    @Override
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
    @Override
    public int getSnippetCount() {
        return snippets.size();
    }

    /*
     * Checks if the collection is empty.
     * @return true if the collection is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return snippets.isEmpty();
    }

    /*
     * Displays information about the collection and all its snippets.
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

    // Legacy method for backward compatibility
    public List<Snippet> getSnippets() {
        return getAllSnippets();
    }

    // Legacy method for backward compatibility
    public int size() {
        return getSnippetCount();
    }
} 