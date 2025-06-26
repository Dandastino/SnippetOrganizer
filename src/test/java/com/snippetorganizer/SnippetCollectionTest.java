package com.snippetorganizer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.snippetorganizer.TestDataUtil.javaSnippet;
import static com.snippetorganizer.TestDataUtil.pythonSnippet;

/**
 * Test suite for the SnippetCollection class.
 * Tests core functionality including Composite Pattern implementation and validation.
 */
class SnippetCollectionTest {

    private SnippetCollection collection;
    private Snippet snippet1;
    private Snippet snippet2;

    @BeforeEach
    void setUp() {
        collection = new SnippetCollection("Test Collection");
        snippet1 = javaSnippet(1);
        snippet2 = pythonSnippet(2);
    }

    @Test
    void testConstructor() {
        assertEquals("Test Collection", collection.getName());
        assertTrue(collection.isEmpty());
        assertEquals(0, collection.getSnippetCount());
    }

    @Test
    void testConstructor_NullName() {
        assertThrows(IllegalArgumentException.class, () ->
            new SnippetCollection(null)
        );
    }

    @Test
    void testAddSnippet() {
        collection.addSnippet(snippet1);
        
        assertEquals(1, collection.getSnippetCount());
        assertFalse(collection.isEmpty());
        
        List<Snippet> snippets = collection.getAllSnippets();
        assertEquals(1, snippets.size());
        assertEquals(snippet1, snippets.get(0));
    }

    @Test
    void testAddSnippet_Null() {
        assertThrows(IllegalArgumentException.class, () ->
            collection.addSnippet(null)
        );
    }

    @Test
    void testAddMultipleSnippets() {
        collection.addSnippet(snippet1);
        collection.addSnippet(snippet2);
        
        assertEquals(2, collection.getSnippetCount());
        assertFalse(collection.isEmpty());
        
        List<Snippet> snippets = collection.getAllSnippets();
        assertEquals(2, snippets.size());
        assertTrue(snippets.contains(snippet1));
        assertTrue(snippets.contains(snippet2));
    }

    @Test
    void testRemoveSnippet() {
        collection.addSnippet(snippet1);
        collection.addSnippet(snippet2);
        
        collection.removeSnippet(snippet1);
        
        assertEquals(1, collection.getSnippetCount());
        List<Snippet> snippets = collection.getAllSnippets();
        assertEquals(1, snippets.size());
        assertEquals(snippet2, snippets.get(0));
    }

    @Test
    void testRemoveSnippet_Null() {
        assertThrows(IllegalArgumentException.class, () ->
            collection.removeSnippet(null)
        );
    }

    @Test
    void testGetAllSnippets_DefensiveCopy() {
        collection.addSnippet(snippet1);
        
        List<Snippet> snippets = collection.getAllSnippets();
        snippets.add(snippet2); // Modify the returned list
        
        // Original collection should not be affected
        assertEquals(1, collection.getSnippetCount());
        assertFalse(collection.getAllSnippets().contains(snippet2));
    }

    @Test
    void testGetAllSnippets_EmptyCollection() {
        List<Snippet> snippets = collection.getAllSnippets();
        assertTrue(snippets.isEmpty());
    }

    @Test
    void testGetName() {
        assertEquals("Test Collection", collection.getName());
    }

    @Test
    void testSetName() {
        collection.setName("New Name");
        assertEquals("New Name", collection.getName());
    }

    @Test
    void testSetName_Null() {
        assertThrows(IllegalArgumentException.class, () ->
            collection.setName(null)
        );
    }

    @Test
    void testGetSnippetCount() {
        assertEquals(0, collection.getSnippetCount());
        
        collection.addSnippet(snippet1);
        assertEquals(1, collection.getSnippetCount());
        
        collection.addSnippet(snippet2);
        assertEquals(2, collection.getSnippetCount());
        
        collection.removeSnippet(snippet1);
        assertEquals(1, collection.getSnippetCount());
    }

    @Test
    void testIsEmpty() {
        assertTrue(collection.isEmpty());
        
        collection.addSnippet(snippet1);
        assertFalse(collection.isEmpty());
        
        collection.removeSnippet(snippet1);
        assertTrue(collection.isEmpty());
    }

    @Test
    void testDisplay_EmptyCollection() {
        assertDoesNotThrow(() -> collection.display());
    }

    @Test
    void testDisplay_WithSnippets() {
        collection.addSnippet(snippet1);
        collection.addSnippet(snippet2);
        
        assertDoesNotThrow(() -> collection.display());
    }

    // Legacy method tests
    @Test
    void testGetSnippets_Legacy() {
        collection.addSnippet(snippet1);
        
        List<Snippet> snippets = collection.getSnippets();
        assertEquals(1, snippets.size());
        assertEquals(snippet1, snippets.get(0));
    }

    @Test
    void testSize_Legacy() {
        assertEquals(0, collection.size());
        
        collection.addSnippet(snippet1);
        assertEquals(1, collection.size());
    }
} 