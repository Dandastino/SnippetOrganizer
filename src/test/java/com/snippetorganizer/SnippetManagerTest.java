package com.snippetorganizer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*
 * Test suite for the SnippetManager class.
 * Tests core functionality including adding, editing, deleting snippets,
 */
class SnippetManagerTest {

    private SnippetManager manager;
    private Path tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = Files.createTempFile("snippets_test_", ".json");
        manager = new SnippetManager(tempFile.toString());
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testAddSnippet() {
        manager.addSnippet("Title1", "Java", "System.out.println(\"Hello World\");");

        List<Snippet> snippets = manager.getAllSnippets();
        assertEquals(1, snippets.size());

        Snippet snippet = snippets.get(0);
        assertEquals("Title1", snippet.getTitle());
        assertEquals("Java", snippet.getLanguage());
        assertEquals("System.out.println(\"Hello World\");", snippet.getCode());
    }

    @Test
    void testAddSnippetWithTagsAndDescription() {
        Set<String> tags = new HashSet<>();
        tags.add("java");
        tags.add("hello");
        tags.add("world");
        
        manager.addSnippet("Title1", "Java", "System.out.println(\"Hello World\");", tags, "A simple hello world program");

        List<Snippet> snippets = manager.getAllSnippets();
        assertEquals(1, snippets.size());

        Snippet snippet = snippets.get(0);
        assertEquals("Title1", snippet.getTitle());
        assertEquals("Java", snippet.getLanguage());
        assertEquals("System.out.println(\"Hello World\");", snippet.getCode());
        assertEquals("A simple hello world program", snippet.getDescription());
        
        Set<String> snippetTags = snippet.getTags();
        assertEquals(3, snippetTags.size());
        assertTrue(snippetTags.contains("java"));
        assertTrue(snippetTags.contains("hello"));
        assertTrue(snippetTags.contains("world"));
    }

    @Test
    void testAddSnippetWithEmptyTags() {
        manager.addSnippet("Title1", "Java", "code", new HashSet<>(), "description");

        Snippet snippet = manager.getAllSnippets().get(0);
        assertTrue(snippet.getTags().isEmpty());
        assertEquals("description", snippet.getDescription());
    }

    @Test
    void testAddSnippetWithNullTags() {
        manager.addSnippet("Title1", "Java", "code", null, "description");

        Snippet snippet = manager.getAllSnippets().get(0);
        assertTrue(snippet.getTags().isEmpty());
        assertEquals("description", snippet.getDescription());
    }

    @Test
    void testAddSnippetWithEmptyDescription() {
        manager.addSnippet("Title1", "Java", "code", new HashSet<>(), "");

        Snippet snippet = manager.getAllSnippets().get(0);
        assertTrue(snippet.getDescription().isEmpty());
    }

    @Test
    void testAddSnippetWithNullDescription() {
        manager.addSnippet("Title1", "Java", "code", new HashSet<>(), null);

        Snippet snippet = manager.getAllSnippets().get(0);
        assertNull(snippet.getDescription());
    }

    @Test
    void testEditSnippet() {
        manager.addSnippet("OldTitle", "Java", "Old Code");

        Snippet original = manager.getAllSnippets().get(0);
        int id = original.getId();

        manager.editSnippet(id, "NewTitle", "Python", "print(\"Updated\")");

        Snippet updated = manager.getAllSnippets().get(0);
        assertEquals("NewTitle", updated.getTitle());
        assertEquals("Python", updated.getLanguage());
        assertEquals("print(\"Updated\")", updated.getCode());
    }

    @Test
    void testDeleteSnippet() {
        manager.addSnippet("DeleteMe", "Java", "code");
        int id = manager.getAllSnippets().get(0).getId();

        manager.deleteSnippet(id);

        assertTrue(manager.getAllSnippets().isEmpty());
    }

    @Test
    void testEditNonExistingSnippetThrows() {
        Exception exception = assertThrows(SnippetException.class, () ->
            manager.editSnippet(999, "a", "b", "c")
        );
        assertTrue(exception.getMessage().contains("No snippet found"));
    }

    @Test
    void testDeleteNonExistingSnippetThrows() {
        Exception exception = assertThrows(SnippetException.class, () ->
            manager.deleteSnippet(999)
        );
        assertTrue(exception.getMessage().contains("No snippet found"));
    }

    @Test
    void testGetAllSnippetsReturnsEmptyInitially() {
        assertTrue(manager.getAllSnippets().isEmpty());
    }

    @Test
    void testSearchSnippets() {
        Set<String> tags = new HashSet<>();
        tags.add("search");
        tags.add("test");
        
        manager.addSnippet("Searchable Title", "Java", "searchable code", tags, "searchable description");
        manager.addSnippet("Other Title", "Python", "other code", new HashSet<>(), "other description");

        // Search by title
        assertDoesNotThrow(() -> manager.searchSnippets("Searchable"));
        
        // Search by code
        assertDoesNotThrow(() -> manager.searchSnippets("searchable"));
        
        // Search by description
        assertDoesNotThrow(() -> manager.searchSnippets("searchable description"));
        
        // Search by tag
        assertDoesNotThrow(() -> manager.searchSnippets("search"));
    }

    @Test
    void testSearchSnippetsEmptyKeyword() {
        assertThrows(SnippetException.class, () ->
            manager.searchSnippets("")
        );
    }

    @Test
    void testSearchSnippetsNullKeyword() {
        assertThrows(SnippetException.class, () ->
            manager.searchSnippets(null)
        );
    }

    @Test
    void testSearchSnippetsWhitespaceKeyword() {
        assertThrows(SnippetException.class, () ->
            manager.searchSnippets("   ")
        );
    }

    @Test
    void testSearchByTag() {
        Set<String> tags1 = new HashSet<>();
        tags1.add("java");
        tags1.add("oop");
        
        Set<String> tags2 = new HashSet<>();
        tags2.add("python");
        tags2.add("scripting");
        
        manager.addSnippet("Java Snippet", "Java", "public class Test {}", tags1, "Java description");
        manager.addSnippet("Python Snippet", "Python", "def test(): pass", tags2, "Python description");

        // Search by tag
        assertDoesNotThrow(() -> manager.searchByTag("java"));
        assertDoesNotThrow(() -> manager.searchByTag("python"));
        assertDoesNotThrow(() -> manager.searchByTag("oop"));
    }

    @Test
    void testSearchByTagEmptyTag() {
        assertThrows(SnippetException.class, () ->
            manager.searchByTag("")
        );
    }

    @Test
    void testSearchByTagNullTag() {
        assertThrows(SnippetException.class, () ->
            manager.searchByTag(null)
        );
    }

    @Test
    void testSearchByTagWhitespaceTag() {
        assertThrows(SnippetException.class, () ->
            manager.searchByTag("   ")
        );
    }

    @Test
    void testGetAllTags() {
        Set<String> tags1 = new HashSet<>();
        tags1.add("java");
        tags1.add("oop");
        
        Set<String> tags2 = new HashSet<>();
        tags2.add("python");
        tags2.add("scripting");
        
        Set<String> tags3 = new HashSet<>();
        tags3.add("java"); // Duplicate tag
        
        manager.addSnippet("Java Snippet", "Java", "code1", tags1, "desc1");
        manager.addSnippet("Python Snippet", "Python", "code2", tags2, "desc2");
        manager.addSnippet("Another Java", "Java", "code3", tags3, "desc3");

        Set<String> allTags = manager.getAllTags();
        
        assertEquals(4, allTags.size());
        assertTrue(allTags.contains("java"));
        assertTrue(allTags.contains("oop"));
        assertTrue(allTags.contains("python"));
        assertTrue(allTags.contains("scripting"));
    }

    @Test
    void testGetAllTagsEmpty() {
        Set<String> allTags = manager.getAllTags();
        assertTrue(allTags.isEmpty());
    }

    @Test
    void testGetSnippetCount() {
        assertEquals(0, manager.getSnippetCount());
        
        manager.addSnippet("Title1", "Java", "code1");
        assertEquals(1, manager.getSnippetCount());
        
        manager.addSnippet("Title2", "Python", "code2");
        assertEquals(2, manager.getSnippetCount());
        
        manager.deleteSnippet(manager.getAllSnippets().get(0).getId());
        assertEquals(1, manager.getSnippetCount());
    }

    @Test
    void testGetSnippetComponent() {
        manager.addSnippet("Title1", "Java", "code1");
        manager.addSnippet("Title2", "Python", "code2");
        
        var component = manager.getSnippetComponent();
        assertNotNull(component);
        assertEquals(2, component.getSnippetCount());
    }

    @Test
    void testListSnippetsEmpty() {
        assertDoesNotThrow(() -> manager.listSnippets());
    }

    @Test
    void testListSnippetsWithContent() {
        manager.addSnippet("Title1", "Java", "code1");
        manager.addSnippet("Title2", "Python", "code2");
        
        assertDoesNotThrow(() -> manager.listSnippets());
    }

    @Test
    void testSnippetIdsAreUnique() {
        manager.addSnippet("Title1", "Java", "code1");
        manager.addSnippet("Title2", "Python", "code2");
        manager.addSnippet("Title3", "JavaScript", "code3");
        
        List<Snippet> snippets = manager.getAllSnippets();
        assertEquals(3, snippets.size());
        
        Set<Integer> ids = new HashSet<>();
        for (Snippet snippet : snippets) {
            ids.add(snippet.getId());
        }
        
        assertEquals(3, ids.size()); // All IDs should be unique
    }

    @Test
    void testSnippetIdsAreSequential() {
        manager.addSnippet("Title1", "Java", "code1");
        manager.addSnippet("Title2", "Python", "code2");
        manager.addSnippet("Title3", "JavaScript", "code3");
        
        List<Snippet> snippets = manager.getAllSnippets();
        assertEquals(3, snippets.size());
        
        // IDs should be sequential starting from 1
        assertEquals(1, snippets.get(0).getId());
        assertEquals(2, snippets.get(1).getId());
        assertEquals(3, snippets.get(2).getId());
    }

    @Test
    void testSnippetIdsAfterDelete() {
        manager.addSnippet("Title1", "Java", "code1");
        manager.addSnippet("Title2", "Python", "code2");
        manager.addSnippet("Title3", "JavaScript", "code3");
        
        // Delete the first snippet
        manager.deleteSnippet(1);
        
        List<Snippet> snippets = manager.getAllSnippets();
        assertEquals(2, snippets.size());
        
        // Remaining snippets should have IDs 2 and 3
        assertEquals(2, snippets.get(0).getId());
        assertEquals(3, snippets.get(1).getId());
        
        // Add a new snippet - should get ID 4
        manager.addSnippet("Title4", "C++", "code4");
        snippets = manager.getAllSnippets();
        assertEquals(3, snippets.size());
        assertEquals(4, snippets.get(2).getId());
    }

    @Test
    void testCaseInsensitiveTagSearch() {
        Set<String> tags = new HashSet<>();
        tags.add("JAVA");
        tags.add("OOP");
        
        manager.addSnippet("Java Snippet", "Java", "code", tags, "description");
        
        // Search should work case-insensitively
        assertDoesNotThrow(() -> manager.searchByTag("java"));
        assertDoesNotThrow(() -> manager.searchByTag("JAVA"));
        assertDoesNotThrow(() -> manager.searchByTag("oop"));
        assertDoesNotThrow(() -> manager.searchByTag("OOP"));
    }

    @Test
    void testSearchWithSpecialCharacters() {
        manager.addSnippet("Special & Characters", "C++", "#include <iostream>", new HashSet<>(), "Special description");
        
        // Search should handle special characters
        assertDoesNotThrow(() -> manager.searchSnippets("Special & Characters"));
        assertDoesNotThrow(() -> manager.searchSnippets("#include"));
        assertDoesNotThrow(() -> manager.searchSnippets("Special description"));
    }

    @Test
    void testSearchWithUnicode() {
        manager.addSnippet("Unicode Title 你好", "Python", "print('你好世界')", new HashSet<>(), "Unicode description 世界");
        
        // Search should handle unicode
        assertDoesNotThrow(() -> manager.searchSnippets("你好"));
        assertDoesNotThrow(() -> manager.searchSnippets("世界"));
    }
}
