package com.snippetorganizer.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.snippetorganizer.composite.SnippetComponent;
import com.snippetorganizer.exception.SnippetException;

/**
 * Analysis utility class for Snippet Organizer data.
 * 
 * @author Sherif Moustafa
 * @version 1.0
 * @see SnippetComponent
 * @see Snippet
 */
public class SnippetAnalyzer {
    
    /**
     * Analyzes a snippet component and returns comprehensive statistics.
     * 
     * @param component the snippet component to analyze (must not be null)
     * @return a map containing various analysis metrics
     * @throws SnippetException if component is null
     */
    public static Map<String, Object> analyzeComponent(SnippetComponent component) {
        if (component == null) {
            throw SnippetException.validationError("Component cannot be null");
        }
        
        Map<String, Object> analysis = new HashMap<>();
        analysis.put("componentName", component.getName());
        analysis.put("totalSnippets", component.getSnippetCount());
        analysis.put("isEmpty", component.isEmpty());
        
        if (!component.isEmpty()) {
            analysis.put("averageCodeLength", getAverageCodeLength(component));
            analysis.put("languageDistribution", getLanguageDistribution(component));
            analysis.put("longestSnippet", getLongestSnippet(component) != null ? getLongestSnippet(component).getTitle() : "None");
            analysis.put("shortestSnippet", getShortestSnippet(component) != null ? getShortestSnippet(component).getTitle() : "None");
        } else {
            analysis.put("averageCodeLength", 0.0);
            analysis.put("languageDistribution", new HashMap<String, Integer>());
            analysis.put("longestSnippet", "None");
            analysis.put("shortestSnippet", "None");
        }
        
        return analysis;
    }
    
    /**
     * Gets the distribution of programming languages in the component.
     * 
     * @param component the snippet component to analyze (must not be null)
     * @return a map of language names to snippet counts
     * @throws SnippetException if component is null
     */
    public static Map<String, Integer> getLanguageDistribution(SnippetComponent component) {
        if (component == null) {
            throw SnippetException.validationError("Component cannot be null");
        }
        
        Map<String, Integer> distribution = new HashMap<>();
        
        for (Snippet snippet : component.getAllSnippets()) {
            String language = snippet.getLanguage();
            distribution.put(language, distribution.getOrDefault(language, 0) + 1);
        }
        
        return distribution;
    }
    
    /**
     * Calculates the average code length in the component.
     * 
     * @param component the snippet component to analyze (must not be null)
     * @return the average code length in characters
     * @throws SnippetException if component is null
     */
    public static double getAverageCodeLength(SnippetComponent component) {
        if (component == null) {
            throw SnippetException.validationError("Component cannot be null");
        }
        
        if (component.isEmpty()) {
            return 0.0;
        }
        
        int totalLength = 0;
        int snippetCount = 0;
        
        for (Snippet snippet : component.getAllSnippets()) {
            totalLength += snippet.getCode().length();
            snippetCount++;
        }
        
        return snippetCount > 0 ? (double) totalLength / snippetCount : 0.0;
    }
    
    /**
     * Finds the snippet with the longest code in the component.
     * 
     * @param component the snippet component to analyze (must not be null)
     * @return the snippet with the longest code, or null if the component is empty
     * @throws SnippetException if component is null
     */
    public static Snippet getLongestSnippet(SnippetComponent component) {
        if (component == null) {
            throw SnippetException.validationError("Component cannot be null");
        }
        
        return component.getAllSnippets().stream()
                .max((s1, s2) -> Integer.compare(s1.getCode().length(), s2.getCode().length()))
                .orElse(null);
    }
    
    /**
     * Finds the snippet with the shortest code in the component.
     * 
     * @param component the snippet component to analyze (must not be null)
     * @return the snippet with the shortest code, or null if the component is empty
     * @throws SnippetException if component is null
     */
    public static Snippet getShortestSnippet(SnippetComponent component) {
        if (component == null) {
            throw SnippetException.validationError("Component cannot be null");
        }
        
        return component.getAllSnippets().stream()
                .min((s1, s2) -> Integer.compare(s1.getCode().length(), s2.getCode().length()))
                .orElse(null);
    }
    
    /**
     * Displays a comprehensive analysis of the component.
     * 
     * @param component the snippet component to analyze (must not be null)
     * @throws SnippetException if component is null
     */
    public static void displayAnalysis(SnippetComponent component) {
        if (component == null) {
            throw SnippetException.validationError("Component cannot be null");
        }
        
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
     * Finds snippets by programming language in the component.
     * 
     * @param component the snippet component to search (must not be null)
     * @param language the language to search for (must not be null)
     * @return a list of snippets in the specified language
     * @throws SnippetException if component or language is null
     */
    public static List<Snippet> findSnippetsByLanguage(SnippetComponent component, String language) {
        if (component == null) {
            throw SnippetException.validationError("Component cannot be null");
        }
        if (language == null || language.trim().isEmpty()) {
            throw SnippetException.validationError("Language cannot be null or empty");
        }
        
        return component.getAllSnippets().stream()
                .filter(snippet -> snippet.getLanguage().equalsIgnoreCase(language))
                .toList();
    }
    
    /**
     * Gets snippets with code longer than a specified length.
     * 
     * @param component the snippet component to search (must not be null)
     * @param minLength the minimum code length in characters
     * @return a list of snippets with code longer than the minimum length
     * @throws SnippetException if component is null or minLength is negative
     */
    public static List<Snippet> getSnippetsWithCodeLongerThan(SnippetComponent component, int minLength) {
        if (component == null) {
            throw SnippetException.validationError("Component cannot be null");
        }
        if (minLength < 0) {
            throw SnippetException.validationError("Minimum length cannot be negative");
        }
        
        return component.getAllSnippets().stream()
                .filter(snippet -> snippet.getCode().length() > minLength)
                .toList();
    }

    /**
     * Gets the distribution of tags in the component.
     * 
     * @param component the snippet component to analyze (must not be null)
     * @return a map of tag names to usage counts
     * @throws SnippetException if component is null
     */
    public static Map<String, Integer> getTagDistribution(SnippetComponent component) {
        if (component == null) {
            throw SnippetException.validationError("Component cannot be null");
        }
        
        Map<String, Integer> distribution = new HashMap<>();
        
        for (Snippet snippet : component.getAllSnippets()) {
            for (String tag : snippet.getTags()) {
                distribution.put(tag, distribution.getOrDefault(tag, 0) + 1);
            }
        }
        
        return distribution;
    }

    /**
     * Gets snippets that have descriptions.
     * 
     * @param component the snippet component to search (must not be null)
     * @return a list of snippets that have descriptions
     * @throws SnippetException if component is null
     */
    public static List<Snippet> getSnippetsWithDescriptions(SnippetComponent component) {
        if (component == null) {
            throw SnippetException.validationError("Component cannot be null");
        }
        
        return component.getAllSnippets().stream()
                .filter(snippet -> snippet.getDescription() != null && !snippet.getDescription().isEmpty())
                .toList();
    }

    /**
     * Gets snippets without descriptions.
     * 
     * @param component the snippet component to search (must not be null)
     * @return a list of snippets that don't have descriptions
     * @throws SnippetException if component is null
     */
    public static List<Snippet> getSnippetsWithoutDescriptions(SnippetComponent component) {
        if (component == null) {
            throw SnippetException.validationError("Component cannot be null");
        }
        
        return component.getAllSnippets().stream()
                .filter(snippet -> snippet.getDescription() == null || snippet.getDescription().isEmpty())
                .toList();
    }

    /**
     * Displays enhanced analysis including tag statistics.
     * 
     * @param component the snippet component to analyze (must not be null)
     * @throws SnippetException if component is null
     */
    public static void displayEnhancedAnalysis(SnippetComponent component) {
        if (component == null) {
            throw SnippetException.validationError("Component cannot be null");
        }
        
        Map<String, Object> analysis = analyzeComponent(component);
        
        System.out.println("=== ENHANCED SNIPPET ANALYSIS ===");
        System.out.println("Component: " + analysis.get("name"));
        System.out.println("Total Snippets: " + analysis.get("totalSnippets"));
        System.out.println("Empty: " + analysis.get("isEmpty"));
        System.out.println("Average Code Length: " + String.format("%.1f", analysis.get("averageCodeLength")) + " characters");
        
        System.out.println("\nLanguage Distribution:");
        @SuppressWarnings("unchecked")
        Map<String, Integer> languageStats = (Map<String, Integer>) analysis.get("languageDistribution");
        languageStats.forEach((language, count) -> 
            System.out.println("  " + language + ": " + count + " snippets"));
        
        Map<String, Integer> tagStats = getTagDistribution(component);
        if (!tagStats.isEmpty()) {
            System.out.println("\nTag Distribution:");
            tagStats.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .forEach(entry -> 
                    System.out.println("  " + entry.getKey() + ": " + entry.getValue() + " snippets"));
        }
        
        // Description statistics
        List<Snippet> withDescriptions = getSnippetsWithDescriptions(component);
        List<Snippet> withoutDescriptions = getSnippetsWithoutDescriptions(component);
        System.out.println("\nDescription Statistics:");
        System.out.println("  Snippets with descriptions: " + withDescriptions.size());
        System.out.println("  Snippets without descriptions: " + withoutDescriptions.size());
        
        System.out.println("\nLongest Snippet: " + analysis.get("longestSnippet"));
        System.out.println("Shortest Snippet: " + analysis.get("shortestSnippet"));
        System.out.println("=================================");
    }
} 