package com.snippetorganizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/*
 * SnippetExporter class provides methods to export snippets to text files.
 * Uses SnippetComponent interface for better abstraction and flexibility.
 */
public class SnippetExporter {
    /*
     * Exports a list of snippets to a single text file.
     * @param snippets the list of snippets to export
     * @param filename the name of the output file
     * @throws IOException if an I/O error occurs
     */
    public static void exportToText(List<Snippet> snippets, String filename) throws IOException {
        StringBuilder content = new StringBuilder();
        for (Snippet snippet : snippets) {
            content.append(snippet.toString()).append("\n\n");
        }
        
        Path filePath = Paths.get(filename);
        Files.writeString(filePath, content.toString());
        SnippetLogger.logInfo("Exported snippets to " + filename);
    }
    
    /*
     * Exports a snippet component to a single text file.
     * @param component the snippet component to export
     * @param filename the name of the output file
     * @throws IOException if an I/O error occurs
     */
    public static void exportComponentToText(SnippetComponent component, String filename) throws IOException {
        StringBuilder content = new StringBuilder();
        content.append("=== EXPORTED FROM: ").append(component.getName()).append(" ===\n");
        content.append("Total Snippets: ").append(component.getSnippetCount()).append("\n\n");
        
        for (Snippet snippet : component.getAllSnippets()) {
            content.append(snippet.toString()).append("\n\n");
        }
        
        Path filePath = Paths.get(filename);
        Files.writeString(filePath, content.toString());
        SnippetLogger.logInfo("Exported component '" + component.getName() + "' to " + filename);
    }
    
    /*
     * Exports snippets by language to separate files.
     * @param component the snippet component to export
     * @param baseFilename the base filename (language will be appended)
     * @throws IOException if an I/O error occurs
     */
    public static void exportByLanguage(SnippetComponent component, String baseFilename) throws IOException {
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
    
    /*
     * Exports a summary report of the component.
     * @param component the snippet component to export
     * @param filename the name of the output file
     * @throws IOException if an I/O error occurs
     */
    public static void exportSummaryReport(SnippetComponent component, String filename) throws IOException {
        Map<String, Object> analysis = SnippetAnalyzer.analyzeComponent(component);
        
        StringBuilder content = new StringBuilder();
        content.append("=== SNIPPET SUMMARY REPORT ===\n");
        content.append("Component: ").append(analysis.get("name")).append("\n");
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
        Files.writeString(filePath, content.toString());
        SnippetLogger.logInfo("Exported summary report for component '" + component.getName() + "' to " + filename);
    }
} 