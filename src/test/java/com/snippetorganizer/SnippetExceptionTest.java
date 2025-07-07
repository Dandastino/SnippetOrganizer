package com.snippetorganizer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.snippetorganizer.exception.SnippetException;

/**
 * Test suite for the SnippetException class.
 * Tests core functionality including Exception Shielding Pattern implementation.
 */
class SnippetExceptionTest {

    @Test
    void testConstructor_MessageOnly() {
        String message = "Test exception message";
        SnippetException exception = new SnippetException(message);
        
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructor_MessageAndCause() {
        String message = "Test exception message";
        RuntimeException cause = new RuntimeException("Original cause");
        SnippetException exception = new SnippetException(message, cause);
        
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testConstructor_NullMessage() {
        SnippetException exception = new SnippetException(null);
        
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructor_EmptyMessage() {
        String message = "";
        SnippetException exception = new SnippetException(message);
        
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructor_MessageWithSpecialCharacters() {
        String message = "Exception with special chars: !@#$%^&*()_+-=[]{}|;':\",./<>?";
        SnippetException exception = new SnippetException(message);
        
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructor_MessageWithUnicode() {
        String message = "Unicode exception: 你好世界 🌍";
        SnippetException exception = new SnippetException(message);
        
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructor_NullMessageAndCause() {
        SnippetException exception = new SnippetException(SnippetException.ErrorType.OPERATION_FAILED, null, null);
        
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructor_MessageAndNullCause() {
        String message = "Message with null cause";
        SnippetException exception = new SnippetException(message, null);
        
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructor_WithDifferentCauseTypes() {
        // Test with different exception types as causes
        Exception[] causes = {
            new RuntimeException("Runtime exception"),
            new IllegalArgumentException("Illegal argument"),
            new NullPointerException("Null pointer"),
            new ArrayIndexOutOfBoundsException("Array index out of bounds"),
            new ClassCastException("Class cast exception")
        };
        
        for (Exception cause : causes) {
            SnippetException exception = new SnippetException("Test message", cause);
            assertEquals(cause, exception.getCause());
            assertEquals("Test message", exception.getMessage());
        }
    }

    @Test
    void testConstructor_WithNestedExceptions() {
        // Create a chain of exceptions
        RuntimeException original = new RuntimeException("Original");
        IllegalArgumentException middle = new IllegalArgumentException("Middle", original);
        SnippetException wrapper = new SnippetException("Wrapper", middle);
        
        assertEquals("Wrapper", wrapper.getMessage());
        assertEquals(middle, wrapper.getCause());
        assertEquals(original, wrapper.getCause().getCause());
    }

    @Test
    void testInheritance() {
        SnippetException exception = new SnippetException("Test");
        
        // Verify it's a RuntimeException
        assertTrue(exception instanceof RuntimeException);
        
        // Verify it's also an Exception (RuntimeException extends Exception)
        assertTrue(exception instanceof Exception);
    }

    @Test
    void testExceptionChaining() {
        // Test that exception chaining works properly
        RuntimeException cause = new RuntimeException("Original cause");
        SnippetException exception = new SnippetException("Application error", cause);
        
        // Verify the chain
        assertEquals("Application error", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals("Original cause", exception.getCause().getMessage());
    }

    @Test
    void testMultipleInstances() {
        SnippetException exception1 = new SnippetException("First exception");
        SnippetException exception2 = new SnippetException("Second exception");
        SnippetException exception3 = new SnippetException("Third exception");
        
        // Verify each exception is unique
        assertNotEquals(exception1.getMessage(), exception2.getMessage());
        assertNotEquals(exception2.getMessage(), exception3.getMessage());
        assertNotEquals(exception1.getMessage(), exception3.getMessage());
    }

    @Test
    void testExceptionStackTrace() {
        SnippetException exception = new SnippetException("Test exception");
        
        // Verify stack trace is available
        StackTraceElement[] stackTrace = exception.getStackTrace();
        assertNotNull(stackTrace);
        assertTrue(stackTrace.length > 0);
    }
} 