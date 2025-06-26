package com.snippetorganizer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;


/**
 * Test suite for the SnippetFactory class.
 * Tests core functionality including Factory Pattern implementation and validation.
 */
class SnippetFactoryTest {

    @Test
    void testCreateSnippet_Basic() {
        Snippet snippet = SnippetFactory.createSnippet(1, "Test Snippet", "Java", "public void test() {}");
        
        assertEquals(1, snippet.getId());
        assertEquals("Test Snippet", snippet.getTitle());
        assertEquals("Java", snippet.getLanguage());
        assertEquals("public void test() {}", snippet.getCode());
        assertTrue(snippet.getTags().isEmpty());
        assertTrue(snippet.getDescription().isEmpty());
    }

    @Test
    void testCreateSnippet_WithSpecialCharacters() {
        Snippet snippet = SnippetFactory.createSnippet(2, "Special & Characters", "C++", "#include <iostream>\nstd::cout << \"Hello\";");
        
        assertEquals(2, snippet.getId());
        assertEquals("Special & Characters", snippet.getTitle());
        assertEquals("C++", snippet.getLanguage());
        assertEquals("#include <iostream>\nstd::cout << \"Hello\";", snippet.getCode());
    }

    @Test
    void testCreateSnippet_WithUnicode() {
        Snippet snippet = SnippetFactory.createSnippet(3, "Unicode Title 你好", "Python", "print('你好世界')");
        
        assertEquals(3, snippet.getId());
        assertEquals("Unicode Title 你好", snippet.getTitle());
        assertEquals("Python", snippet.getLanguage());
        assertEquals("print('你好世界')", snippet.getCode());
    }

    @Test
    void testCreateSnippet_WithLongCode() {
        String longCode = """
            public class VeryLongClass {
                public void veryLongMethod() {
                    System.out.println("This is a very long method");
                    System.out.println("with multiple lines of code");
                    System.out.println("to test the factory with large content");
                }
            }
            """;
        
        Snippet snippet = SnippetFactory.createSnippet(4, "Long Code Snippet", "Java", longCode);
        
        assertEquals(4, snippet.getId());
        assertEquals("Long Code Snippet", snippet.getTitle());
        assertEquals("Java", snippet.getLanguage());
        assertEquals(longCode, snippet.getCode());
    }

    @Test
    void testCreateSnippet_EmptyCode() {
        assertThrows(IllegalArgumentException.class, () ->
            SnippetFactory.createSnippet(1, "Empty Code", "Java", "")
        );
    }

    @Test
    void testCreateSnippet_NullCode() {
        assertThrows(IllegalArgumentException.class, () ->
            SnippetFactory.createSnippet(1, "Null Code", "Java", null)
        );
    }

    @Test
    void testCreateSnippet_EmptyTitle() {
        assertThrows(IllegalArgumentException.class, () ->
            SnippetFactory.createSnippet(1, "", "Java", "code")
        );
    }

    @Test
    void testCreateSnippet_NullTitle() {
        assertThrows(IllegalArgumentException.class, () ->
            SnippetFactory.createSnippet(1, null, "Java", "code")
        );
    }

    @Test
    void testCreateSnippet_EmptyLanguage() {
        assertThrows(IllegalArgumentException.class, () ->
            SnippetFactory.createSnippet(1, "Title", "", "code")
        );
    }

    @Test
    void testCreateSnippet_NullLanguage() {
        assertThrows(IllegalArgumentException.class, () ->
            SnippetFactory.createSnippet(1, "Title", null, "code")
        );
    }

    @Test
    void testCreateSnippet_MultipleInstances() {
        Snippet snippet1 = SnippetFactory.createSnippet(1, "First", "Java", "code1");
        Snippet snippet2 = SnippetFactory.createSnippet(2, "Second", "Python", "code2");
        Snippet snippet3 = SnippetFactory.createSnippet(3, "Third", "JavaScript", "code3");
        
        // Verify each snippet is unique
        assertNotEquals(snippet1, snippet2);
        assertNotEquals(snippet2, snippet3);
        assertNotEquals(snippet1, snippet3);
        
        // Verify correct data
        assertEquals(1, snippet1.getId());
        assertEquals("First", snippet1.getTitle());
        assertEquals("Java", snippet1.getLanguage());
        assertEquals("code1", snippet1.getCode());
        
        assertEquals(2, snippet2.getId());
        assertEquals("Second", snippet2.getTitle());
        assertEquals("Python", snippet2.getLanguage());
        assertEquals("code2", snippet2.getCode());
        
        assertEquals(3, snippet3.getId());
        assertEquals("Third", snippet3.getTitle());
        assertEquals("JavaScript", snippet3.getLanguage());
        assertEquals("code3", snippet3.getCode());
    }

    @Test
    void testCreateSnippet_WithDifferentLanguages() {
        String[] languages = {"Java", "Python", "JavaScript", "C++", "C#", "Ruby", "Go", "Rust", "Swift", "Kotlin"};
        
        for (int i = 0; i < languages.length; i++) {
            Snippet snippet = SnippetFactory.createSnippet(i, "Test " + i, languages[i], "code" + i);
            assertEquals(languages[i], snippet.getLanguage());
        }
    }
} 