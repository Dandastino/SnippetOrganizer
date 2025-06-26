package com.snippetorganizer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.snippetorganizer.TestDataUtil.javaSnippet;

/**
 * Test suite for the Snippet class.
 * Tests core functionality including tags, descriptions, and validation.
 */
class SnippetTest {

    private Snippet basicSnippet;
    private Snippet fullSnippet;

    @BeforeEach
    void setUp() {
        basicSnippet = javaSnippet(1);
        basicSnippet = TestDataUtil.javaSnippet(1);
        fullSnippet = TestDataUtil.pythonSnippet(2);
    }

    @Test
    void testBasicConstructor() {
        assertEquals(1, basicSnippet.getId());
        assertEquals("Java Class", basicSnippet.getTitle());
        assertEquals("Java", basicSnippet.getLanguage());
        assertEquals("public class Test {}", basicSnippet.getCode());
        assertEquals(TestDataUtil.javaTags(), basicSnippet.getTags());
        assertEquals("A Java class", basicSnippet.getDescription());
    }

    @Test
    void testFullConstructor() {
        assertEquals(2, fullSnippet.getId());
        assertEquals("Python Function", fullSnippet.getTitle());
        assertEquals("Python", fullSnippet.getLanguage());
        assertEquals("def test(): pass", fullSnippet.getCode());
        assertEquals("A Python function", fullSnippet.getDescription());
        assertEquals(TestDataUtil.pythonTags(), fullSnippet.getTags());
    }

    @Test
    void testAddTag() {
        basicSnippet.addTag("newtag");
        assertTrue(basicSnippet.hasTag("newtag"));
    }

    @Test
    void testAddTag_CaseInsensitive() {
        basicSnippet.addTag("UPPERCASE");
        assertTrue(basicSnippet.hasTag("uppercase"));
        assertTrue(basicSnippet.hasTag("UPPERCASE"));
    }

    @Test
    void testAddTag_Null() {
        Set<String> before = new HashSet<>(basicSnippet.getTags());
        basicSnippet.addTag(null);
        assertEquals(before, basicSnippet.getTags());
    }

    @Test
    void testRemoveTag() {
        basicSnippet.addTag("testtag");
        basicSnippet.removeTag("testtag");
        assertFalse(basicSnippet.hasTag("testtag"));
    }

    @Test
    void testRemoveTag_CaseInsensitive() {
        basicSnippet.addTag("TESTTAG");
        basicSnippet.removeTag("testtag");
        assertFalse(basicSnippet.hasTag("TESTTAG"));
    }

    @Test
    void testHasTag() {
        basicSnippet.addTag("testtag");
        assertTrue(basicSnippet.hasTag("testtag"));
        assertFalse(basicSnippet.hasTag("nonexistent"));
    }

    @Test
    void testHasTag_CaseInsensitive() {
        basicSnippet.addTag("TESTTAG");
        assertTrue(basicSnippet.hasTag("testtag"));
        assertTrue(basicSnippet.hasTag("TESTTAG"));
    }

    @Test
    void testSetDescription() {
        basicSnippet.setDescription("New description");
        assertEquals("New description", basicSnippet.getDescription());
    }

    @Test
    void testSetDescription_Null() {
        basicSnippet.setDescription(null);
        assertTrue(basicSnippet.getDescription().isEmpty());
    }

    @Test
    void testGetName() {
        assertEquals("Java Class", basicSnippet.getName());
    }

    @Test
    void testGetAllSnippets() {
        List<Snippet> snippets = basicSnippet.getAllSnippets();
        assertEquals(1, snippets.size());
        assertEquals(basicSnippet, snippets.get(0));
    }

    @Test
    void testAddSnippet_ThrowsException() {
        assertThrows(UnsupportedOperationException.class, () ->
            basicSnippet.addSnippet(new Snippet(3, "Test", "Java", "code"))
        );
    }

    @Test
    void testRemoveSnippet_ThrowsException() {
        assertThrows(UnsupportedOperationException.class, () ->
            basicSnippet.removeSnippet(new Snippet(3, "Test", "Java", "code"))
        );
    }

    @Test
    void testGetSnippetCount() {
        assertEquals(1, basicSnippet.getSnippetCount());
    }

    @Test
    void testIsEmpty() {
        assertFalse(basicSnippet.isEmpty());
    }

    @Test
    void testToString() {
        String result = basicSnippet.toString();
        assertTrue(result.contains("Java Class"));
        assertTrue(result.contains("Java"));
        assertTrue(result.contains("public class Test {}"));
    }
} 