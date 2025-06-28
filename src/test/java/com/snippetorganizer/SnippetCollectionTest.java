package com.snippetorganizer;

import java.util.List;
import java.util.Set;

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
        assertThrows(SnippetException.class, () -> {
            new SnippetCollection(null);
        });
    }

    @Test
    void testConstructor_EmptyName() {
        assertThrows(SnippetException.class, () -> {
            new SnippetCollection("");
        });
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
        SnippetCollection collection = new SnippetCollection("Test Collection");
        assertThrows(SnippetException.class, () -> {
            collection.addSnippet((Snippet) null);
        });
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
        SnippetCollection collection = new SnippetCollection("Test Collection");
        assertThrows(SnippetException.class, () -> {
            collection.removeSnippet((Snippet) null);
        });
    }

    @Test
    void testRemoveSnippet_Valid() {
        SnippetCollection collection = new SnippetCollection("Test Collection");
        Snippet snippet = SnippetFactory.createSnippet(1, "Test Snippet", "Java", "public class Test {}");
        
        collection.addSnippet(snippet);
        assertEquals(1, collection.getSnippetCount());
        
        collection.removeSnippet(snippet);
        assertEquals(0, collection.getSnippetCount());
        assertTrue(collection.isEmpty());
    }

    @Test
    void testRemoveSnippet_NotInCollection() {
        SnippetCollection collection = new SnippetCollection("Test Collection");
        Snippet snippet = SnippetFactory.createSnippet(1, "Test Snippet", "Java", "public class Test {}");
        
        // Should not throw exception when removing non-existent snippet
        collection.removeSnippet(snippet);
        assertEquals(0, collection.getSnippetCount());
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
    void testGetAllSnippets_Empty() {
        SnippetCollection collection = new SnippetCollection("Test Collection");
        assertTrue(collection.getAllSnippets().isEmpty());
    }

    @Test
    void testGetAllSnippets_WithSnippets() {
        SnippetCollection collection = new SnippetCollection("Test Collection");
        Snippet snippet1 = SnippetFactory.createSnippet(1, "Test Snippet 1", "Java", "public class Test1 {}");
        Snippet snippet2 = SnippetFactory.createSnippet(2, "Test Snippet 2", "Python", "def test(): pass");
        
        collection.addSnippet(snippet1);
        collection.addSnippet(snippet2);
        
        var snippets = collection.getAllSnippets();
        assertEquals(2, snippets.size());
        assertTrue(snippets.contains(snippet1));
        assertTrue(snippets.contains(snippet2));
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
        SnippetCollection collection = new SnippetCollection("Test Collection");
        assertThrows(SnippetException.class, () -> {
            collection.setName(null);
        });
    }

    @Test
    void testSetName_Empty() {
        SnippetCollection collection = new SnippetCollection("Test Collection");
        assertThrows(SnippetException.class, () -> {
            collection.setName("");
        });
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
    void testDisplay_Empty() {
        SnippetCollection collection = new SnippetCollection("Test Collection");
        // Should not throw exception when displaying empty collection
        collection.display();
    }

    @Test
    void testDisplay_WithSnippets() {
        SnippetCollection collection = new SnippetCollection("Test Collection");
        Snippet snippet1 = SnippetFactory.createSnippet(1, "Java Class", "Java", "public class Test {}", Set.of("oop", "java"), "A Java class");
        Snippet snippet2 = SnippetFactory.createSnippet(2, "Python Function", "Python", "def test(): pass", Set.of("scripting", "python"), "A Python function");
        
        collection.addSnippet(snippet1);
        collection.addSnippet(snippet2);
        
        // Should not throw exception when displaying collection with snippets
        collection.display();
    }
} 