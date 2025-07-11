package com.snippetorganizer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.snippetorganizer.logging.SnippetLogger;

/**
 * Test suite for the SnippetLogger class.
 * Tests core functionality including logging and error handling.
 */
class SnippetLoggerTest {

    private static final String LOG_FILE = "snippet_organizer.log";
    private Path logPath;

    @BeforeEach
    void setUp() throws Exception {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        logPath = dataDir.toPath().resolve(LOG_FILE);
        // Clean up log file before each test
        if (Files.exists(logPath)) {
            Files.delete(logPath);
        }
    }

    @Test
    void testLog_BasicMessage() throws IOException {
        String testMessage = "Test log message";
        SnippetLogger.log(testMessage);
        
        assertTrue(Files.exists(logPath));
        String content = Files.readString(logPath);
        assertTrue(content.contains(testMessage));
        assertTrue(content.contains(getCurrentTimestamp()));
    }

    @Test
    void testLog_MultipleMessages() throws IOException {
        SnippetLogger.log("First message");
        SnippetLogger.log("Second message");
        SnippetLogger.log("Third message");
        
        String content = Files.readString(logPath);
        assertTrue(content.contains("First message"));
        assertTrue(content.contains("Second message"));
        assertTrue(content.contains("Third message"));
        
        // Count lines to verify all messages were written
        String[] lines = content.split("\n");
        assertEquals(3, lines.length);
    }

    @Test
    void testLog_NullMessage() {
        assertThrows(IllegalArgumentException.class, () ->
            SnippetLogger.log(null)
        );
    }

    @Test
    void testLog_EmptyMessage() throws IOException {
        SnippetLogger.log("");
        
        assertTrue(Files.exists(logPath));
        String content = Files.readString(logPath);
        assertTrue(content.contains(getCurrentTimestamp()));
    }

    @Test
    void testLogInfo() throws IOException {
        String infoMessage = "Information message";
        SnippetLogger.logInfo(infoMessage);
        
        String content = Files.readString(logPath);
        assertTrue(content.contains("INFO: " + infoMessage));
        assertTrue(content.contains(getCurrentTimestamp()));
    }

    @Test
    void testLogInfo_NullMessage() {
        assertThrows(IllegalArgumentException.class, () ->
            SnippetLogger.logInfo(null)
        );
    }

    @Test
    void testLogError_WithException() throws IOException {
        String errorMessage = "Error occurred";
        Exception exception = new RuntimeException("Test exception");
        
        SnippetLogger.logError(errorMessage, exception);
        
        String content = Files.readString(logPath);
        assertTrue(content.contains("ERROR: " + errorMessage));
        assertTrue(content.contains("Test exception"));
        assertTrue(content.contains(getCurrentTimestamp()));
    }

    @Test
    void testLogError_WithoutException() throws IOException {
        String errorMessage = "Error without exception";
        SnippetLogger.logError(errorMessage, null);
        
        String content = Files.readString(logPath);
        assertTrue(content.contains("ERROR: " + errorMessage));
        assertTrue(content.contains(getCurrentTimestamp()));
    }

    @Test
    void testLogError_NullMessage() {
        assertThrows(IllegalArgumentException.class, () ->
            SnippetLogger.logError(null, new RuntimeException("test"))
        );
    }

    @Test
    void testLogFileCreation() throws IOException {
        // Ensure log file doesn't exist initially
        assertFalse(Files.exists(logPath));
        
        SnippetLogger.log("Test message");
        
        // Verify file was created
        assertTrue(Files.exists(logPath));
        assertTrue(Files.isRegularFile(logPath));
    }

    @Test
    void testLogFileAppend() throws IOException {
        // Write first message
        SnippetLogger.log("First message");
        String content1 = Files.readString(logPath);
        
        // Write second message
        SnippetLogger.log("Second message");
        String content2 = Files.readString(logPath);
        
        // Verify both messages are present
        assertTrue(content2.contains("First message"));
        assertTrue(content2.contains("Second message"));
        
        // Verify second content is longer (appended)
        assertTrue(content2.length() > content1.length());
    }

    @Test
    void testLogFileFormat() throws IOException {
        SnippetLogger.log("Test message");
        
        String content = Files.readString(logPath);
        String[] lines = content.split("\n");
        // Trim the line to remove any trailing \r or whitespace
        assertTrue(lines[0].trim().matches("\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\] .*"));
    }

    @Test
    void testLogInfoFormat() throws IOException {
        SnippetLogger.logInfo("Test info");
        
        String content = Files.readString(logPath);
        assertTrue(content.contains("["));
        assertTrue(content.contains("]"));
        assertTrue(content.contains("INFO: Test info"));
    }

    @Test
    void testLogErrorFormat() throws IOException {
        Exception exception = new RuntimeException("Test exception");
        SnippetLogger.logError("Test error", exception);
        
        String content = Files.readString(logPath);
        assertTrue(content.contains("["));
        assertTrue(content.contains("]"));
        assertTrue(content.contains("ERROR: Test error"));
        assertTrue(content.contains("Test exception"));
    }

    @Test
    void testLogWithSpecialCharacters() throws IOException {
        String specialMessage = "Message with special chars: !@#$%^&*()_+-=[]{}|;':\",./<>?";
        SnippetLogger.log(specialMessage);
        
        String content = Files.readString(logPath);
        assertTrue(content.contains(specialMessage));
    }

    @Test
    void testLogWithUnicodeCharacters() throws IOException {
        String unicodeMessage = "Unicode message: 你好世界 🌍";
        SnippetLogger.log(unicodeMessage);
        
        String content = Files.readString(logPath);
        assertTrue(content.contains(unicodeMessage));
    }

    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
} 