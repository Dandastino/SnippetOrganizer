import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.snippetorganizer.Snippet;
import com.snippetorganizer.SnippetExporter;

class SnippetExporterTest {

    private Path tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = Files.createTempFile("snippets_test_", ".txt");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testExportToText_CreatesFileWithCorrectContent() throws IOException {
        Snippet snippet1 = new Snippet(1, "Test Title", "Java", "System.out.println(\"Hello\");");
        Snippet snippet2 = new Snippet(2, "Another Snippet", "Python", "print(\"Hi\")");

        List<Snippet> snippets = List.of(snippet1, snippet2);

        SnippetExporter.exportToText(snippets, tempFile.toString());

        assertTrue(Files.exists(tempFile));

        String content = Files.readString(tempFile);

        assertTrue(content.contains("Test Title"));
        assertTrue(content.contains("Another Snippet"));
        assertTrue(content.contains("System.out.println(\"Hello\");"));
        assertTrue(content.contains("print(\"Hi\")"));
    }

    @Test
    void testExportToText_WithEmptyListCreatesEmptyFile() throws IOException {
        SnippetExporter.exportToText(List.of(), tempFile.toString());

        assertTrue(Files.exists(tempFile));
        String content = Files.readString(tempFile);
        assertTrue(content.trim().isEmpty());
    }
}
