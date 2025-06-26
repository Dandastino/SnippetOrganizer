package com.snippetorganizer;

/**
 * Custom exception class for the Snippet Organizer application.
 * 
 * @author Sherif Moustafa
 * @version 1.0
 * @see RuntimeException
 */
public class SnippetException extends RuntimeException {
    
    /**
     * Constructs a new SnippetException with the specified detail message.
     * 
     * @param message the detail message explaining the error
     */
    public SnippetException(String message) {
        super(message);
    }

    /**
     * Constructs a new SnippetException with the specified detail message and cause.
     * 
     * @param message the detail message explaining the error
     * @param cause the cause of the exception (which is saved for later retrieval)
     */
    public SnippetException(String message, Throwable cause) {
        super(message, cause);
    }
} 