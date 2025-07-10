package com.snippetorganizer.core;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.snippetorganizer.composite.SnippetCollection;
import com.snippetorganizer.composite.SnippetComponent;
import com.snippetorganizer.exception.SnippetException;

/**
 * Represents a code snippet with comprehensive metadata and functionality.
 * 
 * @author Sherif Moustafa
 * @version 1.0
 * @see SnippetComponent
 * @see SnippetCollection
 */
public final class Snippet implements SnippetComponent {

    /** Attributes for the snippet */
    private int id;
    private String title;
    private String language;
    private String code;
    private Set<String> tags;
    private String description;

    /**
     * Constructs a new Snippet with basic information.
     * 
     * @param id the unique identifier for the snippet (must be non-negative)
     * @param title the title of the snippet (must not be null or empty)
     * @param language the programming language of the snippet (must not be null or empty)
     * @param code the actual code content (must not be null or empty)
     * @throws SnippetException if any parameter is invalid
     */
    public Snippet(int id, String title, String language, String code) {
        this.id = id;
        setTitle(title);
        setLanguage(language);
        setCode(code);
        this.tags = new HashSet<>();
        this.description = null;
    }

    /**
     * Constructs a new Snippet with complete information including tags and description.
     * 
     * @param id the unique identifier for the snippet (must be non-negative)
     * @param title the title of the snippet (must not be null or empty)
     * @param language the programming language of the snippet (must not be null or empty)
     * @param code the actual code content (must not be null or empty)
     * @param tags the tags for categorization (can be null or empty)
     * @param description the description of the snippet (can be null or empty)
     * @throws SnippetException if required parameters are invalid
     */
    // telling Jakson which constructor to use and how to map JSON fields to constructor parameters. for deserializzation
    @JsonCreator
    public Snippet(
        @JsonProperty("id") int id,
        @JsonProperty("title") String title,
        @JsonProperty("language") String language,
        @JsonProperty("code") String code,
        @JsonProperty("tags") Set<String> tags,
        @JsonProperty("description") String description
    ) {
        this.id = id;
        setTitle(title);
        setLanguage(language);
        setCode(code);
        setTags(tags);
        setDescription(description);
    }

    /**
     * Gets the code content of the snippet.
     * 
     * @return the code content of the snippet
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the code content of the snippet.
     * 
     * @param code the code content to set (must not be null or empty)
     * @throws SnippetException if the code is null or empty
     */
    public void setCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw SnippetException.validationError("Code cannot be null or empty");
        }
        this.code = code;
    }

    /**
     * Gets the unique identifier of the snippet.
     * 
     * @return the ID of the snippet
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the snippet.
     * 
     * @param id the ID to set (must be non-negative)
     * @throws SnippetException if the ID is negative
     */
    public void setId(int id) {
        if (id < 0) {
            throw SnippetException.validationError("ID cannot be negative");
        }
        this.id = id;
    }

    /**
     * Gets the title of the snippet.
     * 
     * @return the title of the snippet
     */
    public String getTitle() {
        return title;
    } 

    /**
     * Sets the title of the snippet.
     * 
     * @param title the title to set (must not be null or empty)
     * @throws SnippetException if the title is null or empty
     */
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw SnippetException.validationError("Title cannot be null or empty");
        }
        this.title = title;
    }
    
    /**
     * Gets the programming language of the snippet.
     * 
     * @return the programming language of the snippet
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the programming language of the snippet.
     * 
     * @param language the programming language to set (must not be null or empty)
     * @throws SnippetException if the language is null or empty
     */
    public void setLanguage(String language) {
        if (language == null || language.trim().isEmpty()) {
            throw SnippetException.validationError("Language cannot be null or empty");
        }
        this.language = language;
    }

    /**
     * Gets a copy of the tags associated with this snippet.
     * s
     * @return a new set containing all tags of the snippet
     */
    public Set<String> getTags() {
        return new HashSet<>(tags);
    }

    /**
     * Sets the tags for this snippet.
     * 
     * @param tags the tags to set (can be null, in which case an empty set is used)
     */
    public void setTags(Set<String> tags) {
        this.tags = tags != null ? new HashSet<>(tags) : new HashSet<>();
    }

    /**
     * Adds a tag to this snippet.
     * 
     * @param tag the tag to add (null or empty tags are ignored)
     */
    public void addTag(String tag) {
        if (tag != null && !tag.trim().isEmpty()) {
            this.tags.add(tag.trim().toLowerCase());
        }
    }

    /**
     * Removes a tag from this snippet.
     * 
     * @param tag the tag to remove
     */
    public void removeTag(String tag) {
        this.tags.remove(tag.toLowerCase());
    }

    /**
     * Checks if this snippet has a specific tag.
     * 
     * @param tag the tag to check for
     * @return true if the snippet has the tag, false otherwise
     */
    public boolean hasTag(String tag) {
        return this.tags.contains(tag.toLowerCase());
    }

    /**
     * Gets the description of this snippet.
     * 
     * @return the description of the snippet (never null, may be empty)
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this snippet.
     * 
     * @param description the description to set (can be null)
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the name of this component.
     * 
     * @return the title of the snippet
     */
    @JsonIgnore
    @Override
    public String getName() {
        return title;
    }

    /**
     * Gets all snippets in this component.
     * 
     * @return a list containing only this snippet
     */
    @JsonIgnore
    @Override
    public List<Snippet> getAllSnippets() {
        return List.of(this);
    }

    /**
     * Adds a snippet to this component (not supported for leaf nodes).
     * 
     * @param snippet the snippet to add (ignored)
     * @throws UnsupportedOperationException always thrown for leaf nodes
     */
    @JsonIgnore
    @Override
    public void addSnippet(Snippet snippet) {
        throw new UnsupportedOperationException("Individual snippets cannot contain other snippets");
    }

    /**
     * Removes a snippet from this component (not supported for leaf nodes).
     * 
     * @param snippet the snippet to remove (ignored)
     * @throws UnsupportedOperationException always thrown for leaf nodes
     */
    @JsonIgnore
    @Override
    public void removeSnippet(Snippet snippet) {
        throw new UnsupportedOperationException("Individual snippets cannot contain other snippets");
    }

    /**
     * Gets the snippet count for this component.
     * 
     * @return always returns 1 for individual snippets
     */
    @JsonIgnore
    @Override
    public int getSnippetCount() {
        return 1;
    }

    /**
     * Checks if this component is empty.
     * 
     * @return always returns false for individual snippets
     */
    @JsonIgnore
    @Override
    public boolean isEmpty() {
        return false;
    }

    /** Displays information about this snippet. */
    @JsonIgnore
    @Override
    public void display() {
        System.out.println(this.toString());
    }

    /**
     * Returns a string representation of this snippet.
     * 
     * @return a formatted string representation of the snippet
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("""
            Snippet ID: %d
            Title: %s
            Language: %s""", id, title, language));
        
        if (description != null && !description.isEmpty()) {
            sb.append(String.format("\nDescription: %s", description));
        }
        
        if (!tags.isEmpty()) {
            sb.append(String.format("\nTags: %s", String.join(", ", tags)));
        }
        
        sb.append(String.format("\nCode:\n%s", code));
        
        return sb.toString();
    }
}