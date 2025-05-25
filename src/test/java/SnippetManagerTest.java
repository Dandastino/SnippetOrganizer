import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.snippetorganizer.Snippet;
import com.snippetorganizer.SnippetException;
import com.snippetorganizer.SnippetManager;

class SnippetManagerTest {

    private SnippetManager manager;

    @BeforeEach
    void setUp() {
        File file = new File("snippets.json");
        if (file.exists()) file.delete();

        manager = new SnippetManager();
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
}
