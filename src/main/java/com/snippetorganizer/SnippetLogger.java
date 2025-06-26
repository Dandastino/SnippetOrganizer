package com.snippetorganizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logging utility class for the Snippet Organizer application.
 * 
 * @author Sherif Moustafa
 * @version 1.0
 */
public class SnippetLogger {
    
    /** The name of the log file */
    private static final String LOG_FILE = "snippet_organizer.log";
    
    /** The data directory for storing application files */
    private static final String DATA_DIR = "data";
    
    /** The formatter for timestamp formatting */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Logs a general message to the log file with a timestamp.
     * 
     * @param message the message to log (must not be null)
     * @throws IllegalArgumentException if the message is null
     */
    public static void log(String message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        
        try {
            // Create data directory if it doesn't exist
            File dataDir = new File(DATA_DIR);
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
            
            String timestamp = LocalDateTime.now().format(formatter);
            String logEntry = String.format("[%s] %s%n", timestamp, message);
            
            Path logPath = Paths.get(DATA_DIR, LOG_FILE);
            
            // If log file exists and is too large (e.g., > 1MB), delete it
            if (Files.exists(logPath) && Files.size(logPath) > 1024 * 1024) {
                Files.delete(logPath);
            }
            
            Files.write(logPath, logEntry.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }

    /**
     * Logs an error message with a throwable.
     * 
     * @param message the error message (must not be null)
     * @param error the throwable associated with the error (can be null)
     * @throws IllegalArgumentException if the message is null
     */
    public static void logError(String message, Throwable error) {
        if (message == null) {
            throw new IllegalArgumentException("Error message cannot be null");
        }
        
        String errorMessage = "ERROR: " + message;
        if (error != null) {
            errorMessage += " - " + error.getMessage();
        }
        log(errorMessage);
    }

    /**
     * Logs an informational message.
     * 
     * @param message the informational message (must not be null)
     * @throws IllegalArgumentException if the message is null
     */
    public static void logInfo(String message) {
        if (message == null) {
            throw new IllegalArgumentException("Info message cannot be null");
        }
        log("INFO: " + message);
    }
} 