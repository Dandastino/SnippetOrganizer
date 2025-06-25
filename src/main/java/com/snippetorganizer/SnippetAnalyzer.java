package com.snippetorganizer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SnippetAnalyzer provides analysis and statistics for snippet components.
 * Uses the SnippetComponent interface to work with any type of snippet organization.
 */
public class SnippetAnalyzer {
    
    /**
     * Analyzes a snippet component and returns statistics.
     * @param component the snippet component to analyze
     * @return analysis results as a map
     */
    public static Map<String, Object> analyzeComponent(SnippetComponent component) {
        Map<String, Object> analysis = new HashMap<>();
        
        analysis.put("name", component.getName());
        analysis.put("totalSnippets", component.getSnippetCount());
        analysis.put("isEmpty", component.isEmpty());
        
        // Language distribution
        Map<String, Integer> languageStats = getLanguageDistribution(component);
        analysis.put("languageDistribution", languageStats);
        
        // Average code length
        double avgCodeLength = getAverageCodeLength(component);
        analysis.put("averageCodeLength", avgCodeLength);
        
        // Longest and shortest snippets
        Snippet longestSnippet = getLongestSnippet(component);
        Snippet shortestSnippet = getShortestSnippet(component);
        analysis.put("longestSnippet", longestSnippet != null ? longestSnippet.getTitle() : "N/A");
        analysis.put("shortestSnippet", shortestSnippet != null ? shortestSnippet.getTitle() : "N/A");
        
        return analysis;
    }
    
    /**
     * Gets the distribution of programming languages in the component.
     * @param component the snippet component to analyze
     * @return map of language to count
     */
    public static Map<String, Integer> getLanguageDistribution(SnippetComponent component) {
        Map<String, Integer> distribution = new HashMap<>();
        
        for (Snippet snippet : component.getAllSnippets()) {
            String language = snippet.getLanguage();
            distribution.put(language, distribution.getOrDefault(language, 0) + 1);
        }
        
        return distribution;
    }
    
    /**
     * Calculates the average code length in the component.
     * @param component the snippet component to analyze
     * @return average code length
     */
    public static double getAverageCodeLength(SnippetComponent component) {
        List<Snippet> snippets = component.getAllSnippets();
        if (snippets.isEmpty()) {
            return 0.0;
        }
        
        int totalLength = snippets.stream()
                .mapToInt(snippet -> snippet.getCode().length())
                .sum();
        
        return (double) totalLength / snippets.size();
    }
    
    /**
     * Finds the snippet with the longest code in the component.
     * @param component the snippet component to analyze
     * @return the snippet with longest code, or null if empty
     */
    public static Snippet getLongestSnippet(SnippetComponent component) {
        return component.getAllSnippets().stream()
                .max((s1, s2) -> Integer.compare(s1.getCode().length(), s2.getCode().length()))
                .orElse(null);
    }
    
    /**
     * Finds the snippet with the shortest code in the component.
     * @param component the snippet component to analyze
     * @return the snippet with shortest code, or null if empty
     */
    public static Snippet getShortestSnippet(SnippetComponent component) {
        return component.getAllSnippets().stream()
                .min((s1, s2) -> Integer.compare(s1.getCode().length(), s2.getCode().length()))
                .orElse(null);
    }
    
    /**
     * Displays a comprehensive analysis of the component.
     * @param component the snippet component to analyze
     */
    public static void displayAnalysis(SnippetComponent component) {
        Map<String, Object> analysis = analyzeComponent(component);
        
        System.out.println("=== SNIPPET ANALYSIS ===");
        System.out.println("Component: " + analysis.get("name"));
        System.out.println("Total Snippets: " + analysis.get("totalSnippets"));
        System.out.println("Empty: " + analysis.get("isEmpty"));
        System.out.println("Average Code Length: " + String.format("%.1f", analysis.get("averageCodeLength")) + " characters");
        
        System.out.println("\nLanguage Distribution:");
        @SuppressWarnings("unchecked")
        Map<String, Integer> languageStats = (Map<String, Integer>) analysis.get("languageDistribution");
        languageStats.forEach((language, count) -> 
            System.out.println("  " + language + ": " + count + " snippets"));
        
        System.out.println("\nLongest Snippet: " + analysis.get("longestSnippet"));
        System.out.println("Shortest Snippet: " + analysis.get("shortestSnippet"));
        System.out.println("=========================");
    }
    
    /**
     * Finds snippets by language in the component.
     * @param component the snippet component to search
     * @param language the language to search for
     * @return list of snippets in the specified language
     */
    public static List<Snippet> findSnippetsByLanguage(SnippetComponent component, String language) {
        return component.getAllSnippets().stream()
                .filter(snippet -> snippet.getLanguage().equalsIgnoreCase(language))
                .toList();
    }
    
    /**
     * Gets snippets with code longer than a specified length.
     * @param component the snippet component to search
     * @param minLength the minimum code length
     * @return list of snippets with code longer than minLength
     */
    public static List<Snippet> getSnippetsWithCodeLongerThan(SnippetComponent component, int minLength) {
        return component.getAllSnippets().stream()
                .filter(snippet -> snippet.getCode().length() > minLength)
                .toList();
    }
} 