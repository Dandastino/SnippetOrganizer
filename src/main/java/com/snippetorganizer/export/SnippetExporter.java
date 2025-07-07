package com.snippetorganizer.export;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.snippetorganizer.core.Snippet;
import com.snippetorganizer.composite.SnippetComponent;
import com.snippetorganizer.core.SnippetAnalyzer;
import com.snippetorganizer.exception.SnippetException;
import com.snippetorganizer.logging.SnippetLogger;

/**
 * Export utility class for Snippet Organizer data.
 * 
 * @author Sherif Moustafa
 * @version 1.0
 * @see SnippetComponent
 * @see Snippet
 * @see SnippetAnalyzer
 */
public class SnippetExporter {
    
    /** The data directory for storing application files */
    private static final String DATA_DIR = "data";
    
    /**
     * Exports a list of snippets to a single text file.
     * 
     * @param snippets the list of snippets to export (must not be null)
     * @param filename the name of the output file (must not be null or empty)
     * @throws IOException if an I/O error occurs during file writing
     * @throws SnippetException if snippets list or filename is null
     */
    public static void exportToText(List<Snippet> snippets, String filename) throws IOException {
        if (snippets == null) {
            throw SnippetException.validationError("Snippets list cannot be null");
        }
        if (filename == null || filename.trim().isEmpty()) {
            throw SnippetException.validationError("Filename cannot be null or empty");
        }
        
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        
        StringBuilder content = new StringBuilder();
        for (Snippet snippet : snippets) {
            content.append(snippet.toString()).append("\n\n");
        }
        
        Path filePath = Paths.get(filename);
        if (!filePath.isAbsolute()) {
            filePath = Paths.get(DATA_DIR, filename);
        }
        // Ensure parent directory exists
        if (filePath.getParent() != null && !Files.exists(filePath.getParent())) {
            Files.createDirectories(filePath.getParent());
        }
        Files.writeString(filePath, content.toString());
        SnippetLogger.logInfo("Exported " + snippets.size() + " snippets to " + filename);
    }
    
    /**
     * Exports a snippet component to a single text file with metadata.
     * 
     * @param component the snippet component to export (must not be null)
     * @param filename the name of the output file (must not be null or empty)
     * @throws IOException if an I/O error occurs during file writing
     * @throws SnippetException if component or filename is null
     */
    public static void exportComponentToText(SnippetComponent component, String filename) throws IOException {
        if (component == null) {
            throw SnippetException.validationError("Component cannot be null");
        }
        if (filename == null || filename.trim().isEmpty()) {
            throw SnippetException.validationError("Filename cannot be null or empty");
        }
        
        StringBuilder content = new StringBuilder();
        content.append("=== EXPORTED FROM: ").append(component.getName()).append(" ===\n");
        content.append("Total Snippets: ").append(component.getSnippetCount()).append("\n\n");
        
        for (Snippet snippet : component.getAllSnippets()) {
            content.append(snippet.toString()).append("\n\n");
        }
        
        Path filePath = Paths.get(filename);
        if (!filePath.isAbsolute()) {
            filePath = Paths.get(DATA_DIR, filename);
        }
        // Ensure parent directory exists
        if (filePath.getParent() != null && !Files.exists(filePath.getParent())) {
            Files.createDirectories(filePath.getParent());
        }
        Files.writeString(filePath, content.toString());
        SnippetLogger.logInfo("Exported component '" + component.getName() + "' to " + filename);
    }
    
    /**
     * Exports snippets by programming language to separate files.
     * 
     * @param component the snippet component to export (must not be null)
     * @param baseFilename the base filename for the language-specific files (must not be null or empty)
     * @throws IOException if an I/O error occurs during file writing
     * @throws SnippetException if component or baseFilename is null
     */
    public static void exportByLanguage(SnippetComponent component, String baseFilename) throws IOException {
        if (component == null) {
            throw SnippetException.validationError("Component cannot be null");
        }
        if (baseFilename == null || baseFilename.trim().isEmpty()) {
            throw SnippetException.validationError("Base filename cannot be null or empty");
        }
        
        Map<String, List<Snippet>> snippetsByLanguage = new HashMap<>();
        
        // Group snippets by language
        for (Snippet snippet : component.getAllSnippets()) {
            String language = snippet.getLanguage();
            snippetsByLanguage.computeIfAbsent(language, k -> new ArrayList<>()).add(snippet);
        }
        
        // Export each language to a separate file
        for (Map.Entry<String, List<Snippet>> entry : snippetsByLanguage.entrySet()) {
            String language = entry.getKey();
            List<Snippet> snippets = entry.getValue();
            
            String filename = baseFilename + "_" + language.toLowerCase() + ".txt";
            exportToText(snippets, filename);
        }
        
        SnippetLogger.logInfo("Exported " + snippetsByLanguage.size() + " language files from component '" + component.getName() + "'");
    }
    
    /**
     * Exports a summary report of the snippet component.
     * 
     * @param component the snippet component to analyze and export (must not be null)
     * @param filename the name of the output file (must not be null or empty)
     * @throws IOException if an I/O error occurs during file writing
     * @throws SnippetException if component or filename is null
     * @see SnippetAnalyzer#analyzeComponent(SnippetComponent)
     */
    public static void exportSummaryReport(SnippetComponent component, String filename) throws IOException {
        if (component == null) {
            throw SnippetException.validationError("Component cannot be null");
        }
        if (filename == null || filename.trim().isEmpty()) {
            throw SnippetException.validationError("Filename cannot be null or empty");
        }
        
        Map<String, Object> analysis = SnippetAnalyzer.analyzeComponent(component);
        
        StringBuilder content = new StringBuilder();
        content.append("=== SNIPPET SUMMARY REPORT ===\n");
        content.append("Component: ").append(analysis.get("componentName")).append("\n");
        content.append("Total Snippets: ").append(analysis.get("totalSnippets")).append("\n");
        content.append("Average Code Length: ").append(String.format("%.1f", analysis.get("averageCodeLength"))).append(" characters\n\n");
        
        content.append("Language Distribution:\n");
        @SuppressWarnings("unchecked")
        Map<String, Integer> languageStats = (Map<String, Integer>) analysis.get("languageDistribution");
        languageStats.forEach((language, count) -> 
            content.append("  ").append(language).append(": ").append(count).append(" snippets\n"));
        
        content.append("\nLongest Snippet: ").append(analysis.get("longestSnippet")).append("\n");
        content.append("Shortest Snippet: ").append(analysis.get("shortestSnippet")).append("\n");
        content.append("===============================\n");
        
        Path filePath = Paths.get(filename);
        if (!filePath.isAbsolute()) {
            filePath = Paths.get(DATA_DIR, filename);
        }
        // Ensure parent directory exists
        if (filePath.getParent() != null && !Files.exists(filePath.getParent())) {
            Files.createDirectories(filePath.getParent());
        }
        Files.writeString(filePath, content.toString());
        SnippetLogger.logInfo("Exported summary report for component '" + component.getName() + "' to " + filename);
    }
} 