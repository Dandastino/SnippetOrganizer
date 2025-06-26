import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Set;
import java.util.HashSet;
import java.util.List;

import com.snippetorganizer.Snippet;

/**
 * Test suite for the Snippet class.
 * Tests core functionality including tags, descriptions, and validation.
 */
class SnippetTest {

    private Snippet basicSnippet;
    private Snippet fullSnippet;

    @BeforeEach
    void setUp() {
        basicSnippet = new Snippet(1, "Test Snippet", "Java", "public void test() {}");
        
        Set<String> tags = new HashSet<>();
        tags.add("test");
        tags.add("java");
        fullSnippet = new Snippet(2, "Full Snippet", "Python", "def test(): pass", tags, "A test snippet");
    }

    @Test
    void testBasicConstructor() {
        assertEquals(1, basicSnippet.getId());
        assertEquals("Test Snippet", basicSnippet.getTitle());
        assertEquals("Java", basicSnippet.getLanguage());
        assertEquals("public void test() {}", basicSnippet.getCode());
        assertTrue(basicSnippet.getTags().isEmpty());
        assertTrue(basicSnippet.getDescription().isEmpty());
    }

    @Test
    void testFullConstructor() {
        assertEquals(2, fullSnippet.getId());
        assertEquals("Full Snippet", fullSnippet.getTitle());
        assertEquals("Python", fullSnippet.getLanguage());
        assertEquals("def test(): pass", fullSnippet.getCode());
        assertEquals("A test snippet", fullSnippet.getDescription());
        
        Set<String> expectedTags = new HashSet<>();
        expectedTags.add("test");
        expectedTags.add("java");
        assertEquals(expectedTags, fullSnippet.getTags());
    }

    @Test
    void testConstructorValidation_NullTitle() {
        assertThrows(IllegalArgumentException.class, () ->
            new Snippet(1, null, "Java", "code")
        );
    }

    @Test
    void testConstructorValidation_EmptyTitle() {
        assertThrows(IllegalArgumentException.class, () ->
            new Snippet(1, "", "Java", "code")
        );
    }

    @Test
    void testConstructorValidation_NullLanguage() {
        assertThrows(IllegalArgumentException.class, () ->
            new Snippet(1, "Title", null, "code")
        );
    }

    @Test
    void testConstructorValidation_NullCode() {
        assertThrows(IllegalArgumentException.class, () ->
            new Snippet(1, "Title", "Java", null)
        );
    }

    @Test
    void testSetId() {
        basicSnippet.setId(5);
        assertEquals(5, basicSnippet.getId());
    }

    @Test
    void testSetId_Negative() {
        assertThrows(IllegalArgumentException.class, () ->
            basicSnippet.setId(-1)
        );
    }

    @Test
    void testSetTitle() {
        basicSnippet.setTitle("New Title");
        assertEquals("New Title", basicSnippet.getTitle());
    }

    @Test
    void testSetTitle_Null() {
        assertThrows(IllegalArgumentException.class, () ->
            basicSnippet.setTitle(null)
        );
    }

    @Test
    void testSetLanguage() {
        basicSnippet.setLanguage("Python");
        assertEquals("Python", basicSnippet.getLanguage());
    }

    @Test
    void testSetLanguage_Null() {
        assertThrows(IllegalArgumentException.class, () ->
            basicSnippet.setLanguage(null)
        );
    }

    @Test
    void testSetCode() {
        basicSnippet.setCode("new code");
        assertEquals("new code", basicSnippet.getCode());
    }

    @Test
    void testSetCode_Null() {
        assertThrows(IllegalArgumentException.class, () ->
            basicSnippet.setCode(null)
        );
    }

    @Test
    void testSetTags() {
        Set<String> newTags = new HashSet<>();
        newTags.add("new");
        newTags.add("tags");
        
        basicSnippet.setTags(newTags);
        assertEquals(newTags, basicSnippet.getTags());
    }

    @Test
    void testSetTags_Null() {
        basicSnippet.setTags(null);
        assertTrue(basicSnippet.getTags().isEmpty());
    }

    @Test
    void testSetTags_DefensiveCopy() {
        Set<String> originalTags = new HashSet<>();
        originalTags.add("original");
        basicSnippet.setTags(originalTags);
        
        // Modify original set
        originalTags.add("modified");
        
        // Snippet tags should not be affected
        Set<String> snippetTags = basicSnippet.getTags();
        assertEquals(1, snippetTags.size());
        assertTrue(snippetTags.contains("original"));
        assertFalse(snippetTags.contains("modified"));
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
        basicSnippet.addTag(null);
        assertTrue(basicSnippet.getTags().isEmpty());
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
        assertEquals("Test Snippet", basicSnippet.getName());
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
        assertTrue(result.contains("Test Snippet"));
        assertTrue(result.contains("Java"));
        assertTrue(result.contains("public void test() {}"));
    }
} 