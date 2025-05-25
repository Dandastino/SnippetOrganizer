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
} 