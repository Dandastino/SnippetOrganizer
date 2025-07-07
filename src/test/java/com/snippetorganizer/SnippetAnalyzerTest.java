package com.snippetorganizer;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.snippetorganizer.core.Snippet;
import com.snippetorganizer.core.SnippetAnalyzer;
import com.snippetorganizer.composite.SnippetComponent;
import com.snippetorganizer.composite.SnippetCollection;
import com.snippetorganizer.exception.SnippetException;
import com.snippetorganizer.factory.SnippetFactory;
import static com.snippetorganizer.TestDataUtil.javaSnippet;
import static com.snippetorganizer.TestDataUtil.jsSnippet;
import static com.snippetorganizer.TestDataUtil.longSnippet;
import static com.snippetorganizer.TestDataUtil.pythonSnippet;
import static com.snippetorganizer.TestDataUtil.shortSnippet;

/**
 * Test suite for the SnippetAnalyzer class.
 * Tests core functionality including statistical analysis and filtering.
 */
class SnippetAnalyzerTest {

    private SnippetComponent component;
    private Snippet javaSnippet;
    private Snippet pythonSnippet;
    private Snippet jsSnippet;
    private Snippet longSnippet;
    private Snippet shortSnippet;

    @BeforeEach
    void setUp() {
        component = new SnippetCollection("Test Collection");
        javaSnippet = javaSnippet(1);
        pythonSnippet = pythonSnippet(2);
        jsSnippet = jsSnippet(3);
        longSnippet = longSnippet(4);
        shortSnippet = shortSnippet(5);
        component.addSnippet(javaSnippet);
        component.addSnippet(pythonSnippet);
        component.addSnippet(jsSnippet);
        component.addSnippet(longSnippet);
        component.addSnippet(shortSnippet);
    }

    @Test
    void testAnalyzeComponent() {
        Map<String, Object> analysis = SnippetAnalyzer.analyzeComponent(component);
        
        assertEquals("Test Collection", analysis.get("componentName"));
        assertEquals(5, analysis.get("totalSnippets"));
        assertEquals(false, analysis.get("isEmpty"));
        
        @SuppressWarnings("unchecked")
        Map<String, Integer> languageStats = (Map<String, Integer>) analysis.get("languageDistribution");
        assertEquals(2, languageStats.get("Java"));
        assertEquals(2, languageStats.get("Python"));
        assertEquals(1, languageStats.get("JavaScript"));
        
        assertTrue((Double) analysis.get("averageCodeLength") > 0);
        assertEquals("Long Code", analysis.get("longestSnippet"));
        assertEquals("Short Code", analysis.get("shortestSnippet"));
    }

    @Test
    void testAnalyzeComponent_Null() {
        assertThrows(SnippetException.class, () -> {
            SnippetAnalyzer.analyzeComponent(null);
        });
    }

    @Test
    void testAnalyzeComponent_Empty() {
        SnippetCollection emptyCollection = new SnippetCollection("Empty");
        Map<String, Object> analysis = SnippetAnalyzer.analyzeComponent(emptyCollection);
        
        assertEquals(0, analysis.get("totalSnippets"));
        assertEquals(0.0, analysis.get("averageCodeLength"));
        assertEquals("Empty", analysis.get("componentName"));
        assertEquals("None", analysis.get("longestSnippet"));
        assertEquals("None", analysis.get("shortestSnippet"));
    }

    @Test
    void testGetLanguageDistribution() {
        Map<String, Integer> distribution = SnippetAnalyzer.getLanguageDistribution(component);
        
        assertEquals(2, distribution.get("Java"));
        assertEquals(2, distribution.get("Python"));
        assertEquals(1, distribution.get("JavaScript"));
        assertEquals(3, distribution.size());
    }

    @Test
    void testGetLanguageDistribution_Empty() {
        SnippetCollection emptyCollection = new SnippetCollection("Empty");
        Map<String, Integer> distribution = SnippetAnalyzer.getLanguageDistribution(emptyCollection);
        
        assertTrue(distribution.isEmpty());
    }

    @Test
    void testGetLanguageDistribution_Null() {
        assertThrows(SnippetException.class, () -> {
            SnippetAnalyzer.getLanguageDistribution(null);
        });
    }

    @Test
    void testGetAverageCodeLength() {
        double avgLength = SnippetAnalyzer.getAverageCodeLength(component);
        assertTrue(avgLength > 0);
        
        // Verify it's a reasonable average
        assertTrue(avgLength > shortSnippet.getCode().length());
        assertTrue(avgLength < longSnippet.getCode().length());
    }

    @Test
    void testGetAverageCodeLength_Empty() {
        SnippetCollection emptyCollection = new SnippetCollection("Empty");
        double avgLength = SnippetAnalyzer.getAverageCodeLength(emptyCollection);
        assertEquals(0.0, avgLength);
    }

    @Test
    void testGetAverageCodeLength_Null() {
        assertThrows(SnippetException.class, () -> {
            SnippetAnalyzer.getAverageCodeLength(null);
        });
    }

    @Test
    void testGetLongestSnippet() {
        Snippet longest = SnippetAnalyzer.getLongestSnippet(component);
        assertEquals(longSnippet, longest);
    }

    @Test
    void testGetLongestSnippet_Empty() {
        SnippetCollection emptyCollection = new SnippetCollection("Empty");
        Snippet longest = SnippetAnalyzer.getLongestSnippet(emptyCollection);
        assertNull(longest);
    }

    @Test
    void testGetLongestSnippet_Null() {
        assertThrows(SnippetException.class, () -> {
            SnippetAnalyzer.getLongestSnippet(null);
        });
    }

    @Test
    void testGetShortestSnippet() {
        Snippet shortest = SnippetAnalyzer.getShortestSnippet(component);
        assertEquals(shortSnippet, shortest);
    }

    @Test
    void testGetShortestSnippet_Empty() {
        SnippetCollection emptyCollection = new SnippetCollection("Empty");
        Snippet shortest = SnippetAnalyzer.getShortestSnippet(emptyCollection);
        assertNull(shortest);
    }

    @Test
    void testGetShortestSnippet_Null() {
        assertThrows(SnippetException.class, () -> {
            SnippetAnalyzer.getShortestSnippet(null);
        });
    }

    @Test
    void testFindSnippetsByLanguage() {
        List<Snippet> javaSnippets = SnippetAnalyzer.findSnippetsByLanguage(component, "Java");
        assertEquals(2, javaSnippets.size());
        assertTrue(javaSnippets.contains(javaSnippet));
        assertTrue(javaSnippets.contains(longSnippet));
        
        List<Snippet> pythonSnippets = SnippetAnalyzer.findSnippetsByLanguage(component, "Python");
        assertEquals(2, pythonSnippets.size());
        assertTrue(pythonSnippets.contains(pythonSnippet));
        assertTrue(pythonSnippets.contains(shortSnippet));
    }

    @Test
    void testFindSnippetsByLanguage_CaseInsensitive() {
        List<Snippet> javaSnippets = SnippetAnalyzer.findSnippetsByLanguage(component, "java");
        assertEquals(2, javaSnippets.size());
        
        List<Snippet> pythonSnippets = SnippetAnalyzer.findSnippetsByLanguage(component, "PYTHON");
        assertEquals(2, pythonSnippets.size());
    }

    @Test
    void testFindSnippetsByLanguage_NonExistent() {
        List<Snippet> cppSnippets = SnippetAnalyzer.findSnippetsByLanguage(component, "C++");
        assertTrue(cppSnippets.isEmpty());
    }

    @Test
    void testFindSnippetsByLanguage_NullComponent() {
        assertThrows(SnippetException.class, () -> {
            SnippetAnalyzer.findSnippetsByLanguage(null, "Java");
        });
    }

    @Test
    void testFindSnippetsByLanguage_NullLanguage() {
        SnippetCollection collection = new SnippetCollection("Test Collection");
        assertThrows(SnippetException.class, () -> {
            SnippetAnalyzer.findSnippetsByLanguage(collection, null);
        });
    }

    @Test
    void testFindSnippetsByLanguage_EmptyLanguage() {
        SnippetCollection collection = new SnippetCollection("Test Collection");
        assertThrows(SnippetException.class, () -> {
            SnippetAnalyzer.findSnippetsByLanguage(collection, "");
        });
    }

    @Test
    void testFindSnippetsByLanguage_NoMatches() {
        SnippetCollection collection = new SnippetCollection("Test Collection");
        Snippet snippet = SnippetFactory.createSnippet(1, "Java Code", "Java", "public class Test {}", Set.of("java"), "Java code");
        collection.addSnippet(snippet);
        
        List<Snippet> matches = SnippetAnalyzer.findSnippetsByLanguage(collection, "Python");
        
        assertTrue(matches.isEmpty());
    }

    @Test
    void testFindSnippetsByLanguage_WithMatches() {
        SnippetCollection collection = new SnippetCollection("Test Collection");
        Snippet snippet1 = SnippetFactory.createSnippet(1, "Java Code 1", "Java", "public class Test1 {}", Set.of("java"), "Java code 1");
        Snippet snippet2 = SnippetFactory.createSnippet(2, "Python Code", "Python", "def test(): pass", Set.of("python"), "Python code");
        Snippet snippet3 = SnippetFactory.createSnippet(3, "Java Code 2", "Java", "public class Test2 {}", Set.of("java"), "Java code 2");
        
        collection.addSnippet(snippet1);
        collection.addSnippet(snippet2);
        collection.addSnippet(snippet3);
        
        List<Snippet> matches = SnippetAnalyzer.findSnippetsByLanguage(collection, "Java");
        
        assertEquals(2, matches.size());
        assertTrue(matches.contains(snippet1));
        assertTrue(matches.contains(snippet3));
    }

    @Test
    void testGetTagDistribution() {
        Map<String, Integer> tagDistribution = SnippetAnalyzer.getTagDistribution(component);
        
        assertEquals(1, tagDistribution.get("java"));
        assertEquals(1, tagDistribution.get("python"));
        assertEquals(1, tagDistribution.get("javascript"));
        assertEquals(1, tagDistribution.get("oop"));
        assertEquals(1, tagDistribution.get("scripting"));
        assertEquals(1, tagDistribution.get("web"));
    }

    @Test
    void testGetTagDistribution_Empty() {
        SnippetCollection emptyCollection = new SnippetCollection("Empty");
        Map<String, Integer> distribution = SnippetAnalyzer.getTagDistribution(emptyCollection);
        
        assertTrue(distribution.isEmpty());
    }

    @Test
    void testGetTagDistribution_Null() {
        assertThrows(SnippetException.class, () -> {
            SnippetAnalyzer.getTagDistribution(null);
        });
    }

    @Test
    void testGetTagDistribution_WithSnippets() {
        SnippetCollection collection = new SnippetCollection("Test Collection");
        Snippet snippet1 = SnippetFactory.createSnippet(1, "Java Code", "Java", "public class Test {}", Set.of("java", "oop"), "Java code");
        Snippet snippet2 = SnippetFactory.createSnippet(2, "Python Code", "Python", "def test(): pass", Set.of("python", "scripting"), "Python code");
        Snippet snippet3 = SnippetFactory.createSnippet(3, "Another Java", "Java", "public void test() {}", Set.of("java", "oop"), "More Java");
        
        collection.addSnippet(snippet1);
        collection.addSnippet(snippet2);
        collection.addSnippet(snippet3);
        
        Map<String, Integer> distribution = SnippetAnalyzer.getTagDistribution(collection);
        
        assertEquals(2, distribution.get("java"));
        assertEquals(2, distribution.get("oop"));
        assertEquals(1, distribution.get("python"));
        assertEquals(1, distribution.get("scripting"));
    }

    @Test
    void testGetSnippetsWithDescriptions() {
        List<Snippet> snippetsWithDescriptions = SnippetAnalyzer.getSnippetsWithDescriptions(component);
        assertEquals(5, snippetsWithDescriptions.size());
        
        for (Snippet snippet : snippetsWithDescriptions) {
            assertFalse(snippet.getDescription().isEmpty());
        }
    }

    @Test
    void testGetSnippetsWithDescriptions_Null() {
        assertThrows(SnippetException.class, () -> {
            SnippetAnalyzer.getSnippetsWithDescriptions(null);
        });
    }

    @Test
    void testGetSnippetsWithDescriptions_Empty() {
        SnippetCollection emptyCollection = new SnippetCollection("Empty");
        List<Snippet> snippets = SnippetAnalyzer.getSnippetsWithDescriptions(emptyCollection);
        
        assertTrue(snippets.isEmpty());
    }

    @Test
    void testGetSnippetsWithDescriptions_WithSnippets() {
        SnippetCollection collection = new SnippetCollection("Test Collection");
        Snippet snippet1 = SnippetFactory.createSnippet(1, "Java Code", "Java", "public class Test {}", Set.of("java"), "Java code");
        Snippet snippet2 = SnippetFactory.createSnippet(2, "Python Code", "Python", "def test(): pass", Set.of("python"), "Python code");
        Snippet snippet3 = SnippetFactory.createSnippet(3, "No Description", "JavaScript", "console.log('test')", Set.of("js"), null);
        
        collection.addSnippet(snippet1);
        collection.addSnippet(snippet2);
        collection.addSnippet(snippet3);
        
        List<Snippet> snippets = SnippetAnalyzer.getSnippetsWithDescriptions(collection);
        
        assertEquals(2, snippets.size());
        assertTrue(snippets.contains(snippet1));
        assertTrue(snippets.contains(snippet2));
        assertFalse(snippets.contains(snippet3));
    }

    @Test
    void testGetSnippetsWithoutDescriptions() {
        // Create a snippet without description
        Snippet noDescSnippet = new Snippet(6, "No Description", "Java", "code");
        component.addSnippet(noDescSnippet);
        
        List<Snippet> snippetsWithoutDescriptions = SnippetAnalyzer.getSnippetsWithoutDescriptions(component);
        assertEquals(1, snippetsWithoutDescriptions.size());
        assertEquals(noDescSnippet, snippetsWithoutDescriptions.get(0));
    }

    @Test
    void testGetSnippetsWithoutDescriptions_Null() {
        assertThrows(SnippetException.class, () -> {
            SnippetAnalyzer.getSnippetsWithoutDescriptions(null);
        });
    }

    @Test
    void testGetSnippetsWithoutDescriptions_Empty() {
        SnippetCollection emptyCollection = new SnippetCollection("Empty");
        List<Snippet> snippets = SnippetAnalyzer.getSnippetsWithoutDescriptions(emptyCollection);
        
        assertTrue(snippets.isEmpty());
    }

    @Test
    void testGetSnippetsWithoutDescriptions_WithSnippets() {
        SnippetCollection collection = new SnippetCollection("Test Collection");
        Snippet snippet1 = SnippetFactory.createSnippet(1, "Java Code", "Java", "public class Test {}", Set.of("java"), "Java code");
        Snippet snippet2 = SnippetFactory.createSnippet(2, "Python Code", "Python", "def test(): pass", Set.of("python"), "Python code");
        Snippet snippet3 = SnippetFactory.createSnippet(3, "No Description", "JavaScript", "console.log('test')", Set.of("js"), null);
        
        collection.addSnippet(snippet1);
        collection.addSnippet(snippet2);
        collection.addSnippet(snippet3);
        
        List<Snippet> snippets = SnippetAnalyzer.getSnippetsWithoutDescriptions(collection);
        
        assertEquals(1, snippets.size());
        assertFalse(snippets.contains(snippet1));
        assertFalse(snippets.contains(snippet2));
        assertTrue(snippets.contains(snippet3));
    }

    @Test
    void testDisplayAnalysis() {
        assertDoesNotThrow(() -> SnippetAnalyzer.displayAnalysis(component));
    }

    @Test
    void testDisplayAnalysis_Null() {
        assertThrows(SnippetException.class, () -> {
            SnippetAnalyzer.displayAnalysis(null);
        });
    }

    @Test
    void testDisplayAnalysis_Empty() {
        SnippetCollection emptyCollection = new SnippetCollection("Empty");
        assertDoesNotThrow(() -> SnippetAnalyzer.displayAnalysis(emptyCollection));
    }

    @Test
    void testDisplayAnalysis_WithSnippets() {
        SnippetCollection collection = new SnippetCollection("Test Collection");
        Snippet snippet1 = SnippetFactory.createSnippet(1, "Short Code", "Java", "a", Set.of("java"), "Short");
        Snippet snippet2 = SnippetFactory.createSnippet(2, "Long Code", "Python", "very long code that exceeds the short one", Set.of("python"), "Long");
        
        collection.addSnippet(snippet1);
        collection.addSnippet(snippet2);
        
        // Should not throw exception when displaying analysis of collection with snippets
        assertDoesNotThrow(() -> SnippetAnalyzer.displayAnalysis(collection));
    }

    @Test
    void testDisplayEnhancedAnalysis() {
        assertDoesNotThrow(() -> SnippetAnalyzer.displayEnhancedAnalysis(component));
    }

    @Test
    void testDisplayEnhancedAnalysis_Null() {
        assertThrows(SnippetException.class, () -> {
            SnippetAnalyzer.displayEnhancedAnalysis(null);
        });
    }

    @Test
    void testDisplayEnhancedAnalysis_Empty() {
        SnippetCollection emptyCollection = new SnippetCollection("Empty");
        // Should not throw exception when displaying enhanced analysis of empty collection
        assertDoesNotThrow(() -> SnippetAnalyzer.displayEnhancedAnalysis(emptyCollection));
    }

    @Test
    void testDisplayEnhancedAnalysis_WithSnippets() {
        SnippetCollection collection = new SnippetCollection("Test Collection");
        Snippet snippet1 = SnippetFactory.createSnippet(1, "Short Code", "Java", "a", Set.of("java"), "Short");
        Snippet snippet2 = SnippetFactory.createSnippet(2, "Long Code", "Python", "very long code that exceeds the short one", Set.of("python"), "Long");
        
        collection.addSnippet(snippet1);
        collection.addSnippet(snippet2);
        
        // Should not throw exception when displaying enhanced analysis of collection with snippets
        assertDoesNotThrow(() -> SnippetAnalyzer.displayEnhancedAnalysis(collection));
    }
} 