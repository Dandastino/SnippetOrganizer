package com.snippetorganizer;

/*
 * SnippetException class represents an exception that occurs in the Snippet Organizer application.
 * It extends the RuntimeException class and provides constructors to create exceptions with a message and/or a cause.
 */
public class SnippetException extends RuntimeException {
    public SnippetException(String message) {
        super(message);
    }

    /*
     * This constructor allows for chaining exceptions, providing both a message and the original cause.
     * @param message the error message
     * @param cause the cause of the exception
     * 
     */
    public SnippetException(String message, Throwable cause) {
        super(message, cause);
    }
} 