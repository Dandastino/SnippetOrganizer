import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import com.snippetorganizer.Snippet;
import com.snippetorganizer.SnippetCollection;
import com.snippetorganizer.SnippetComponent;
import com.snippetorganizer.SnippetAnalyzer;

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
        
        Set<String> javaTags = new HashSet<>();
        javaTags.add("java");
        javaTags.add("oop");
        
        Set<String> pythonTags = new HashSet<>();
        pythonTags.add("python");
        pythonTags.add("scripting");
        
        Set<String> jsTags = new HashSet<>();
        jsTags.add("javascript");
        jsTags.add("web");
        
        javaSnippet = new Snippet(1, "Java Class", "Java", "public class Test {}", javaTags, "A Java class");
        pythonSnippet = new Snippet(2, "Python Function", "Python", "def test(): pass", pythonTags, "A Python function");
        jsSnippet = new Snippet(3, "JS Function", "JavaScript", "function test() {}", jsTags, "A JavaScript function");
        longSnippet = new Snippet(4, "Long Code", "Java", "public class VeryLongClass { public void veryLongMethod() { System.out.println(\"This is a very long method with lots of code and multiple lines\"); } }", new HashSet<>(), "A very long snippet");
        shortSnippet = new Snippet(5, "Short Code", "Python", "x=1", new HashSet<>(), "A very short snippet");
        
        component.addSnippet(javaSnippet);
        component.addSnippet(pythonSnippet);
        component.addSnippet(jsSnippet);
        component.addSnippet(longSnippet);
        component.addSnippet(shortSnippet);
    }

    @Test
    void testAnalyzeComponent() {
        Map<String, Object> analysis = SnippetAnalyzer.analyzeComponent(component);
        
        assertEquals("Test Collection", analysis.get("name"));
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
        assertThrows(IllegalArgumentException.class, () ->
            SnippetAnalyzer.analyzeComponent(null)
        );
    }

    @Test
    void testAnalyzeComponent_Empty() {
        SnippetComponent emptyComponent = new SnippetCollection("Empty");
        Map<String, Object> analysis = SnippetAnalyzer.analyzeComponent(emptyComponent);
        
        assertEquals("Empty", analysis.get("name"));
        assertEquals(0, analysis.get("totalSnippets"));
        assertEquals(true, analysis.get("isEmpty"));
        assertEquals(0.0, analysis.get("averageCodeLength"));
        assertEquals("N/A", analysis.get("longestSnippet"));
        assertEquals("N/A", analysis.get("shortestSnippet"));
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
        SnippetComponent emptyComponent = new SnippetCollection("Empty");
        Map<String, Integer> distribution = SnippetAnalyzer.getLanguageDistribution(emptyComponent);
        
        assertTrue(distribution.isEmpty());
    }

    @Test
    void testGetLanguageDistribution_Null() {
        assertThrows(IllegalArgumentException.class, () ->
            SnippetAnalyzer.getLanguageDistribution(null)
        );
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
        SnippetComponent emptyComponent = new SnippetCollection("Empty");
        double avgLength = SnippetAnalyzer.getAverageCodeLength(emptyComponent);
        assertEquals(0.0, avgLength);
    }

    @Test
    void testGetAverageCodeLength_Null() {
        assertThrows(IllegalArgumentException.class, () ->
            SnippetAnalyzer.getAverageCodeLength(null)
        );
    }

    @Test
    void testGetLongestSnippet() {
        Snippet longest = SnippetAnalyzer.getLongestSnippet(component);
        assertEquals(longSnippet, longest);
    }

    @Test
    void testGetLongestSnippet_Empty() {
        SnippetComponent emptyComponent = new SnippetCollection("Empty");
        Snippet longest = SnippetAnalyzer.getLongestSnippet(emptyComponent);
        assertNull(longest);
    }

    @Test
    void testGetLongestSnippet_Null() {
        assertThrows(IllegalArgumentException.class, () ->
            SnippetAnalyzer.getLongestSnippet(null)
        );
    }

    @Test
    void testGetShortestSnippet() {
        Snippet shortest = SnippetAnalyzer.getShortestSnippet(component);
        assertEquals(shortSnippet, shortest);
    }

    @Test
    void testGetShortestSnippet_Empty() {
        SnippetComponent emptyComponent = new SnippetCollection("Empty");
        Snippet shortest = SnippetAnalyzer.getShortestSnippet(emptyComponent);
        assertNull(shortest);
    }

    @Test
    void testGetShortestSnippet_Null() {
        assertThrows(IllegalArgumentException.class, () ->
            SnippetAnalyzer.getShortestSnippet(null)
        );
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
        assertThrows(IllegalArgumentException.class, () ->
            SnippetAnalyzer.findSnippetsByLanguage(null, "Java")
        );
    }

    @Test
    void testFindSnippetsByLanguage_NullLanguage() {
        assertThrows(IllegalArgumentException.class, () ->
            SnippetAnalyzer.findSnippetsByLanguage(component, null)
        );
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
        SnippetComponent emptyComponent = new SnippetCollection("Empty");
        Map<String, Integer> distribution = SnippetAnalyzer.getTagDistribution(emptyComponent);
        
        assertTrue(distribution.isEmpty());
    }

    @Test
    void testGetTagDistribution_Null() {
        assertThrows(IllegalArgumentException.class, () ->
            SnippetAnalyzer.getTagDistribution(null)
        );
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
        assertThrows(IllegalArgumentException.class, () ->
            SnippetAnalyzer.getSnippetsWithDescriptions(null)
        );
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
        assertThrows(IllegalArgumentException.class, () ->
            SnippetAnalyzer.getSnippetsWithoutDescriptions(null)
        );
    }

    @Test
    void testDisplayAnalysis() {
        assertDoesNotThrow(() -> SnippetAnalyzer.displayAnalysis(component));
    }

    @Test
    void testDisplayAnalysis_Empty() {
        SnippetComponent emptyComponent = new SnippetCollection("Empty");
        assertDoesNotThrow(() -> SnippetAnalyzer.displayAnalysis(emptyComponent));
    }

    @Test
    void testDisplayAnalysis_Null() {
        assertThrows(IllegalArgumentException.class, () ->
            SnippetAnalyzer.displayAnalysis(null)
        );
    }

    @Test
    void testDisplayEnhancedAnalysis() {
        assertDoesNotThrow(() -> SnippetAnalyzer.displayEnhancedAnalysis(component));
    }

    @Test
    void testDisplayEnhancedAnalysis_Empty() {
        SnippetComponent emptyComponent = new SnippetCollection("Empty");
        assertDoesNotThrow(() -> SnippetAnalyzer.displayEnhancedAnalysis(emptyComponent));
    }

    @Test
    void testDisplayEnhancedAnalysis_Null() {
        assertThrows(IllegalArgumentException.class, () ->
            SnippetAnalyzer.displayEnhancedAnalysis(null)
        );
    }
} 