package com.snippetorganizer;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Collection class for managing groups of code snippets.
 * Implements the Composite pattern to support both individual snippets and nested collections.
 * 
 * @author Sherif Moustafa
 * @version 1.0
 * @see SnippetComponent
 * @see Snippet
 */
public class SnippetCollection implements SnippetComponent {
    
    /** The list of components in this collection (can be snippets or other collections) */
    private final List<SnippetComponent> components;
    
    /** The name of this collection */
    private String name;

    /**
     * Constructs a new SnippetCollection with the specified name.
     * 
     * @param name the name of the collection (must not be null or empty)
     * @throws SnippetException if the name is null or empty
     */
    public SnippetCollection(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw SnippetException.validationError("Collection name cannot be null or empty");
        }
        this.name = name;
        this.components = new ArrayList<>();
    }

    /**
     * Adds a snippet to this collection.
     * 
     * @param snippet the snippet to add (must not be null)
     * @throws SnippetException if the snippet is null
     */
    @Override
    public void addSnippet(Snippet snippet) {
        if (snippet == null) {
            throw SnippetException.validationError("Snippet cannot be null");
        }
        components.add(snippet);
    }

    /**
     * Adds a snippet component to this collection.
     * This method supports the Composite pattern by accepting both individual snippets
     * and other collections.
     * 
     * @param component the snippet component to add (must not be null)
     * @throws SnippetException if the component is null
     */
    @Override
    public void addSnippet(SnippetComponent component) {
        if (component == null) {
            throw SnippetException.validationError("Component cannot be null");
        }
        components.add(component);
    }

    /**
     * Removes a snippet from this collection.
     * 
     * @param snippet the snippet to remove (must not be null)
     * @throws SnippetException if the snippet is null
     */
    @Override
    public void removeSnippet(Snippet snippet) {
        if (snippet == null) {
            throw SnippetException.validationError("Snippet cannot be null");
        }
        components.remove(snippet);
    }

    /**
     * Removes a snippet component from this collection.
     * 
     * @param component the snippet component to remove (must not be null)
     * @throws SnippetException if the component is null
     */
    @Override
    public void removeSnippet(SnippetComponent component) {
        if (component == null) {
            throw SnippetException.validationError("Component cannot be null");
        }
        components.remove(component);
    }

    /**
     * Returns a list of all snippets in this collection and all nested collections.
     * This method recursively traverses the composite structure.
     * 
     * @return a new list containing all snippets in this collection and nested collections
     */
    @Override
    public List<Snippet> getAllSnippets() {
        List<Snippet> allSnippets = new ArrayList<>();
        for (SnippetComponent component : components) {
            allSnippets.addAll(component.getAllSnippets());
        }
        return allSnippets;
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
     * @throws SnippetException if the name is null or empty
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw SnippetException.validationError("Collection name cannot be null or empty");
        }
        this.name = name;
    }

    /**
     * Returns the total number of snippets in this collection and all nested collections.
     * This method recursively counts snippets in the composite structure.
     * 
     * @return the total number of snippets in the collection and nested collections
     */
    @Override
    public int getSnippetCount() {
        int totalCount = 0;
        for (SnippetComponent component : components) {
            totalCount += component.getSnippetCount();
        }
        return totalCount;
    }

    /**
     * Checks if this collection is empty.
     * @return true if the collection is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return components.isEmpty();
    }

    /**
     * Displays information about this collection and all its components.
     * This method recursively displays the composite structure.
     */
    @JsonIgnore
    @Override
    public void display() {
        System.out.println("Collection: " + name);
        System.out.println("Total components: " + components.size());
        System.out.println("Total snippets: " + getSnippetCount());
        System.out.println("---------------------------");
        
        if (components.isEmpty()) {
            System.out.println("No components in this collection.");
        } else {
            for (SnippetComponent component : components) {
                component.display();
                System.out.println("---------------------------");
            }
        }
    }

    /**
     * Gets all components in this collection.
     * 
     * @return a list of all components in this collection
     */
    public List<SnippetComponent> getComponents() {
        return new ArrayList<>(components);
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
     * Legacy method for backward compatibility.
     * @return the number of snippets in this collection
     * @deprecated Use {@link #getSnippetCount()} instead
     */
    @Deprecated
    public int size() {
        return getSnippetCount();
    }
} 