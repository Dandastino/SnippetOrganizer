package com.snippetorganizer.exception;

/**
 * Custom exception class for the Snippet Organizer application.
 * Implements the Exception Shielding pattern to provide consistent error handling.
 * 
 * @author Sherif Moustafa
 * @version 1.0
 * @see RuntimeException
 */
public class SnippetException extends RuntimeException {
    
    /** Error types for categorization */
    public enum ErrorType {
        VALIDATION_ERROR("Validation Error"),
        IO_ERROR("I/O Error"),
        NOT_FOUND("Not Found"),
        DUPLICATE_ERROR("Duplicate Error"),
        OPERATION_FAILED("Operation Failed"),
        SYSTEM_ERROR("System Error");
        
        private final String displayName;
        
        ErrorType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    private final ErrorType errorType;
    
    /**
     * Constructs a new SnippetException with the specified detail message.
     * 
     * @param message the detail message explaining the error
     */
    public SnippetException(String message) {
        super(message);
        this.errorType = ErrorType.OPERATION_FAILED;
    }

    /**
     * Constructs a new SnippetException with the specified detail message and cause.
     * 
     * @param message the detail message explaining the error
     * @param cause the cause of the exception (which is saved for later retrieval)
     */
    public SnippetException(String message, Throwable cause) {
        super(message, cause);
        this.errorType = ErrorType.OPERATION_FAILED;
    }
    
    /**
     * Constructs a new SnippetException with the specified error type and message.
     * 
     * @param errorType the type of error that occurred
     * @param message the detail message explaining the error
     */
    public SnippetException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }
    
    /**
     * Constructs a new SnippetException with the specified error type, message, and cause.
     * 
     * @param errorType the type of error that occurred
     * @param message the detail message explaining the error
     * @param cause the cause of the exception (which is saved for later retrieval)
     */
    public SnippetException(ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }
    
    /**
     * Gets the error type of this exception.
     * 
     * @return the error type
     */
    public ErrorType getErrorType() {
        return errorType;
    }
    
    /**
     * Gets a formatted error message including the error type.
     * 
     * @return a formatted error message
     */
    public String getFormattedMessage() {
        return String.format("[%s] %s", errorType.getDisplayName(), getMessage());
    }
    
    /**
     * Creates a validation error exception.
     * 
     * @param message the validation error message
     * @return a new SnippetException with VALIDATION_ERROR type
     */
    public static SnippetException validationError(String message) {
        return new SnippetException(ErrorType.VALIDATION_ERROR, message);
    }
    
    /**
     * Creates an I/O error exception.
     * 
     * @param message the I/O error message
     * @param cause the underlying I/O exception
     * @return a new SnippetException with IO_ERROR type
     */
    public static SnippetException ioError(String message, Throwable cause) {
        return new SnippetException(ErrorType.IO_ERROR, message, cause);
    }
    
    /**
     * Creates a not found error exception.
     * 
     * @param message the not found error message
     * @return a new SnippetException with NOT_FOUND type
     */
    public static SnippetException notFound(String message) {
        return new SnippetException(ErrorType.NOT_FOUND, message);
    }
    
    /**
     * Creates a duplicate error exception.
     * 
     * @param message the duplicate error message
     * @return a new SnippetException with DUPLICATE_ERROR type
     */
    public static SnippetException duplicateError(String message) {
        return new SnippetException(ErrorType.DUPLICATE_ERROR, message);
    }
    
    /**
     * Creates a system error exception.
     * 
     * @param message the system error message
     * @param cause the underlying system exception
     * @return a new SnippetException with SYSTEM_ERROR type
     */
    public static SnippetException systemError(String message, Throwable cause) {
        return new SnippetException(ErrorType.SYSTEM_ERROR, message, cause);
    }
} 