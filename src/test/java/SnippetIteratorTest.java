import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

import com.snippetorganizer.Snippet;
import com.snippetorganizer.SnippetIterator;

/**
 * Test suite for the SnippetIterator class.
 * Tests core functionality including Iterator Pattern implementation.
 */
class SnippetIteratorTest {

    private Snippet snippet1;
    private Snippet snippet2;
    private Snippet snippet3;
    private List<Snippet> snippetList;

    @BeforeEach
    void setUp() {
        Set<String> tags1 = new HashSet<>();
        tags1.add("java");
        tags1.add("test");
        
        snippet1 = new Snippet(1, "Java Test", "Java", "public void test() {}", tags1, "A Java test");
        snippet2 = new Snippet(2, "Python Test", "Python", "def test(): pass", new HashSet<>(), "A Python test");
        snippet3 = new Snippet(3, "JavaScript Test", "JavaScript", "function test() {}", new HashSet<>(), "A JS test");
        
        snippetList = List.of(snippet1, snippet2, snippet3);
    }

    @Test
    void testConstructor() {
        SnippetIterator iterator = new SnippetIterator(snippetList);
        assertNotNull(iterator);
    }

    @Test
    void testConstructor_NullList() {
        assertThrows(IllegalArgumentException.class, () ->
            new SnippetIterator(null)
        );
    }

    @Test
    void testHasNext_EmptyList() {
        SnippetIterator iterator = new SnippetIterator(List.of());
        assertFalse(iterator.hasNext());
    }

    @Test
    void testHasNext_SingleElement() {
        SnippetIterator iterator = new SnippetIterator(List.of(snippet1));
        assertTrue(iterator.hasNext());
        
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    void testHasNext_MultipleElements() {
        SnippetIterator iterator = new SnippetIterator(snippetList);
        
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    void testNext_EmptyList() {
        SnippetIterator iterator = new SnippetIterator(List.of());
        assertNull(iterator.next());
    }

    @Test
    void testNext_SingleElement() {
        SnippetIterator iterator = new SnippetIterator(List.of(snippet1));
        
        Snippet result = iterator.next();
        assertEquals(snippet1, result);
        assertNull(iterator.next());
    }

    @Test
    void testNext_MultipleElements() {
        SnippetIterator iterator = new SnippetIterator(snippetList);
        
        assertEquals(snippet1, iterator.next());
        assertEquals(snippet2, iterator.next());
        assertEquals(snippet3, iterator.next());
        assertNull(iterator.next());
    }

    @Test
    void testNext_BeyondEnd() {
        SnippetIterator iterator = new SnippetIterator(List.of(snippet1));
        
        iterator.next(); // Get the first element
        assertNull(iterator.next()); // Should return null
        assertNull(iterator.next()); // Should still return null
    }

    @Test
    void testIterationOrder() {
        SnippetIterator iterator = new SnippetIterator(snippetList);
        
        // Verify iteration order matches list order
        assertEquals(snippet1, iterator.next());
        assertEquals(snippet2, iterator.next());
        assertEquals(snippet3, iterator.next());
    }

    @Test
    void testMultipleIterations() {
        SnippetIterator iterator1 = new SnippetIterator(snippetList);
        SnippetIterator iterator2 = new SnippetIterator(snippetList);
        
        // Both iterators should work independently
        assertEquals(snippet1, iterator1.next());
        assertEquals(snippet1, iterator2.next());
        
        assertEquals(snippet2, iterator1.next());
        assertEquals(snippet2, iterator2.next());
    }

    @Test
    void testCompleteIteration() {
        SnippetIterator iterator = new SnippetIterator(snippetList);
        int count = 0;
        
        while (iterator.hasNext()) {
            Snippet snippet = iterator.next();
            assertNotNull(snippet);
            count++;
        }
        
        assertEquals(3, count);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }
} 