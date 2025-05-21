package com.snippetorganizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
 * SnippetLogger class provides methods to log messages to a file.
 * It includes methods for logging errors and informational messages.
 */
public class SnippetLogger {
    private static final String LOG_FILE = "snippet_organizer.log";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /*
     * Logs a message to the log file with a timestamp.
     * @param message the message to log
     */
    public static void log(String message) {
        try {
            String timestamp = LocalDateTime.now().format(formatter);
            String logEntry = String.format("[%s] %s%n", timestamp, message);
            
            Path logPath = Paths.get(LOG_FILE);
            if (!Files.exists(logPath)) {
                Files.createFile(logPath);
            }
            
            Files.write(logPath, logEntry.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }

    /*
     * Logs an error message with a throwable.
     * @param message the error message
     * @param error the throwable associated with the error
     */
    public static void logError(String message, Throwable error) {
        log("ERROR: " + message + " - " + error.getMessage());
    }

    /*
     * Logs an informational message.
     * @param message the informational message
     */
    public static void logInfo(String message) {
        log("INFO: " + message);
    }
} 