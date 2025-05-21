package com.snippetorganizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/*
 * SnippetExporter class provides methods to export snippets to text files.
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
     * Exports snippets to separate text files based on their Language.
     * @param snippets the list of snippets to export
     * @param baseDir the base directory for exporting
     * @throws IOException if an I/O error occurs
     */
    public static void exportByCategory(List<Snippet> snippets, String baseDir) throws IOException {
        // Create base directory if it doesn't exist
        Path basePath = Paths.get(baseDir);
        if (!Files.exists(basePath)) {
            Files.createDirectories(basePath);
        }

        // Group snippets by category
        for (Snippet snippet : snippets) {
            String language = snippet.getLanguage();
            if (language != null && !language.isEmpty()) {
                Path languagePath = basePath.resolve(language);
                if (!Files.exists(languagePath)) {
                    Files.createDirectory(languagePath);
                }

                String filename = String.format("%s/%s.txt", language, snippet.getTitle().replaceAll("[^a-zA-Z0-9]", "_"));
                Path filePath = languagePath.resolve(filename);
                Files.writeString(filePath, snippet.toString());
            }
        }
        SnippetLogger.logInfo("Exported snippets by category to " + baseDir);
    }
} 