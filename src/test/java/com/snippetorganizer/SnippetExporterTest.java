package com.snippetorganizer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*
 * Test suite for the SnippetExporter class.
 * Tests core functionality including exporting snippets to text files,
 */
class SnippetExporterTest {

    private Path tempFile;
    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        tempFile = Files.createTempFile("snippets_test_", ".txt");
        tempDir = Files.createTempDirectory("export_test_");
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

    @Test
    void testExportToText_WithTagsAndDescription() throws IOException {
        Set<String> tags = new HashSet<>();
        tags.add("java");
        tags.add("test");
        
        Snippet snippet = new Snippet(1, "Test Snippet", "Java", "public void test() {}", tags, "A test snippet");
        List<Snippet> snippets = List.of(snippet);

        SnippetExporter.exportToText(snippets, tempFile.toString());

        String content = Files.readString(tempFile);
        assertTrue(content.contains("Test Snippet"));
        assertTrue(content.contains("A test snippet"));
        assertTrue(content.contains("java"));
        assertTrue(content.contains("test"));
    }

    @Test
    void testExportToText_NullSnippets() {
        assertThrows(IllegalArgumentException.class, () ->
            SnippetExporter.exportToText(null, tempFile.toString())
        );
    }

    @Test
    void testExportToText_NullFilename() {
        List<Snippet> snippets = List.of(new Snippet(1, "Test", "Java", "code"));
        assertThrows(IllegalArgumentException.class, () ->
            SnippetExporter.exportToText(snippets, null)
        );
    }

    @Test
    void testExportToText_EmptyFilename() {
        List<Snippet> snippets = List.of(new Snippet(1, "Test", "Java", "code"));
        assertThrows(IllegalArgumentException.class, () ->
            SnippetExporter.exportToText(snippets, "")
        );
    }

    @Test
    void testExportComponentToText() throws IOException {
        SnippetComponent component = new SnippetCollection("Test Collection");
        
        Set<String> tags = new HashSet<>();
        tags.add("java");
        tags.add("test");
        
        Snippet snippet = new Snippet(1, "Test Snippet", "Java", "public void test() {}", tags, "A test snippet");
        component.addSnippet(snippet);

        SnippetExporter.exportComponentToText(component, tempFile.toString());

        assertTrue(Files.exists(tempFile));
        String content = Files.readString(tempFile);
        
        assertTrue(content.contains("=== EXPORTED FROM: Test Collection ==="));
        assertTrue(content.contains("Total Snippets: 1"));
        assertTrue(content.contains("Test Snippet"));
        assertTrue(content.contains("A test snippet"));
        assertTrue(content.contains("java"));
        assertTrue(content.contains("test"));
    }

    @Test
    void testExportComponentToText_EmptyComponent() throws IOException {
        SnippetComponent component = new SnippetCollection("Empty Collection");

        SnippetExporter.exportComponentToText(component, tempFile.toString());

        assertTrue(Files.exists(tempFile));
        String content = Files.readString(tempFile);
        
        assertTrue(content.contains("=== EXPORTED FROM: Empty Collection ==="));
        assertTrue(content.contains("Total Snippets: 0"));
    }

    @Test
    void testExportComponentToText_NullComponent() {
        assertThrows(IllegalArgumentException.class, () ->
            SnippetExporter.exportComponentToText(null, tempFile.toString())
        );
    }

    @Test
    void testExportComponentToText_NullFilename() {
        SnippetComponent component = new SnippetCollection("Test");
        assertThrows(IllegalArgumentException.class, () ->
            SnippetExporter.exportComponentToText(component, null)
        );
    }

    @Test
    void testExportComponentToText_EmptyFilename() {
        SnippetComponent component = new SnippetCollection("Test");
        assertThrows(IllegalArgumentException.class, () ->
            SnippetExporter.exportComponentToText(component, "")
        );
    }

    @Test
    void testExportByLanguage() throws IOException {
        SnippetComponent component = new SnippetCollection("Mixed Collection");
        
        component.addSnippet(new Snippet(1, "Java Snippet", "Java", "public class Test {}", new HashSet<>(), "Java code"));
        component.addSnippet(new Snippet(2, "Python Snippet", "Python", "def test(): pass", new HashSet<>(), "Python code"));
        component.addSnippet(new Snippet(3, "Another Java", "Java", "public void test() {}", new HashSet<>(), "More Java"));

        String baseFilename = tempDir.resolve("snippets").toString();
        SnippetExporter.exportByLanguage(component, baseFilename);

        // Check that language-specific files were created
        Path javaFile = Path.of(baseFilename + "_java.txt");
        Path pythonFile = Path.of(baseFilename + "_python.txt");
        
        assertTrue(Files.exists(javaFile));
        assertTrue(Files.exists(pythonFile));
        
        // Check Java file content
        String javaContent = Files.readString(javaFile);
        assertTrue(javaContent.contains("Java Snippet"));
        assertTrue(javaContent.contains("Another Java"));
        assertFalse(javaContent.contains("Python Snippet"));
        
        // Check Python file content
        String pythonContent = Files.readString(pythonFile);
        assertTrue(pythonContent.contains("Python Snippet"));
        assertFalse(pythonContent.contains("Java Snippet"));
    }

    @Test
    void testExportByLanguage_SingleLanguage() throws IOException {
        SnippetComponent component = new SnippetCollection("Java Collection");
        
        component.addSnippet(new Snippet(1, "Java 1", "Java", "code1"));
        component.addSnippet(new Snippet(2, "Java 2", "Java", "code2"));

        String baseFilename = tempDir.resolve("java_snippets").toString();
        SnippetExporter.exportByLanguage(component, baseFilename);

        Path javaFile = Path.of(baseFilename + "_java.txt");
        assertTrue(Files.exists(javaFile));
        
        String content = Files.readString(javaFile);
        assertTrue(content.contains("Java 1"));
        assertTrue(content.contains("Java 2"));
    }

    @Test
    void testExportByLanguage_EmptyComponent() throws IOException {
        SnippetComponent component = new SnippetCollection("Empty");

        String baseFilename = tempDir.resolve("empty").toString();
        SnippetExporter.exportByLanguage(component, baseFilename);

        // Should not create any files for empty component
        assertFalse(Files.exists(Path.of(baseFilename + "_java.txt")));
    }

    @Test
    void testExportByLanguage_NullComponent() {
        assertThrows(IllegalArgumentException.class, () ->
            SnippetExporter.exportByLanguage(null, "test")
        );
    }

    @Test
    void testExportByLanguage_NullBaseFilename() {
        SnippetComponent component = new SnippetCollection("Test");
        assertThrows(IllegalArgumentException.class, () ->
            SnippetExporter.exportByLanguage(component, null)
        );
    }

    @Test
    void testExportByLanguage_EmptyBaseFilename() {
        SnippetComponent component = new SnippetCollection("Test");
        assertThrows(IllegalArgumentException.class, () ->
            SnippetExporter.exportByLanguage(component, "")
        );
    }

    @Test
    void testExportSummaryReport() throws IOException {
        SnippetComponent component = new SnippetCollection("Test Collection");
        
        component.addSnippet(new Snippet(1, "Java Snippet", "Java", "public class Test {}", new HashSet<>(), "Java code"));
        component.addSnippet(new Snippet(2, "Python Snippet", "Python", "def test(): pass", new HashSet<>(), "Python code"));
        component.addSnippet(new Snippet(3, "Long Snippet", "Java", "import java.util.*; public class LibrarySystem { private List<Book> books; private List<User> users; private List<Transaction> transactions; public LibrarySystem() { books = new ArrayList<>(); users = new ArrayList<>(); transactions = new ArrayList<>(); } public void addBook(String title, String author, int year) { Book book = new Book(title, author, year); books.add(book); System.out.println(\"Book added: \" + book); } public void removeBook(UUID bookId) { books.removeIf(book -> book.getId().equals(bookId)); System.out.println(\"Book removed: \" + bookId); } public List<Book> searchBooks(String keyword) { List<Book> results = new ArrayList<>(); for (Book book : books) { if (book.getTitle().toLowerCase().contains(keyword.toLowerCase()) || book.getAuthor().toLowerCase().contains(keyword.toLowerCase())) { results.add(book); } } return results; } public void registerUser(String name, String email) { User user = new User(name, email); users.add(user); System.out.println(\"User registered: \" + user); } public void removeUser(UUID userId) { users.removeIf(user -> user.getId().equals(userId)); System.out.println(\"User removed: \" + userId); } public User getUserByEmail(String email) { for (User user : users) { if (user.getEmail().equalsIgnoreCase(email)) { return user; } } return null; } public void borrowBook(UUID userId, UUID bookId) { User user = findUserById(userId); Book book = findBookById(bookId); if (user == null || book == null || book.isBorrowed()) { System.out.println(\"Borrowing failed.\"); return; } book.setBorrowed(true); Transaction tx = new Transaction(user, book, new Date()); transactions.add(tx); System.out.println(\"Book borrowed: \" + tx); } public void returnBook(UUID userId, UUID bookId) { for (Transaction tx : transactions) { if (tx.getUser().getId().equals(userId) && tx.getBook().getId().equals(bookId) && tx.getReturnDate() == null) { tx.setReturnDate(new Date()); tx.getBook().setBorrowed(false); System.out.println(\"Book returned: \" + tx); return; } } System.out.println(\"Return failed: no matching transaction.\"); } private Book findBookById(UUID id) { for (Book b : books) { if (b.getId().equals(id)) return b; } return null; } private User findUserById(UUID id) { for (User u : users) { if (u.getId().equals(id)) return u; } return null; } private static class Book { private UUID id; private String title; private String author; private int year; private boolean isBorrowed; public Book(String title, String author, int year) { this.id = UUID.randomUUID(); this.title = title; this.author = author; this.year = year; this.isBorrowed = false; } public UUID getId() { return id; } public String getTitle() { return title; } public String getAuthor() { return author; } public boolean isBorrowed() { return isBorrowed; } public void setBorrowed(boolean borrowed) { isBorrowed = borrowed; } public String toString() { return String.format(\"%s by %s (%d)\", title, author, year); } } private static class User { private UUID id; private String name; private String email; public User(String name, String email) { this.id = UUID.randomUUID(); this.name = name; this.email = email; } public UUID getId() { return id; } public String getName() { return name; } public String getEmail() { return email; } public String toString() { return name + \" (\" + email + \")\"; } } private static class Transaction { private User user; private Book book; private Date borrowDate; private Date returnDate; public Transaction(User user, Book book, Date borrowDate) { this.user = user; this.book = book; this.borrowDate = borrowDate; } public User getUser() { return user; } public Book getBook() { return book; } public Date getBorrowDate() { return borrowDate; } public Date getReturnDate() { return returnDate; } public void setReturnDate(Date returnDate) { this.returnDate = returnDate; } public String toString() { return String.format(\"%s borrowed \\\"%s\\\" on %s%s\", user.getName(), book.getTitle(), borrowDate.toString(), returnDate != null ? \", returned on \" + returnDate.toString() : \"\"); } } public static void main(String[] args) { LibrarySystem system = new LibrarySystem(); system.registerUser(\"Alice\", \"alice@example.com\"); system.registerUser(\"Bob\", \"bob@example.com\"); system.addBook(\"The Great Gatsby\", \"F. Scott Fitzgerald\", 1925); system.addBook(\"1984\", \"George Orwell\", 1949); User alice = system.getUserByEmail(\"alice@example.com\"); Book gatsby = system.searchBooks(\"gatsby\").get(0); system.borrowBook(alice.getId(), gatsby.getId()); system.returnBook(alice.getId(), gatsby.getId()); } };", new HashSet<>(), "A very long snippet"));

        SnippetExporter.exportSummaryReport(component, tempFile.toString());

        assertTrue(Files.exists(tempFile));
        String content = Files.readString(tempFile);
        
        assertTrue(content.contains("=== SNIPPET SUMMARY REPORT ==="));
        assertTrue(content.contains("Component: Test Collection"));
        assertTrue(content.contains("Total Snippets: 3"));
        assertTrue(content.contains("Language Distribution:"));
        assertTrue(content.contains("Java: 2 snippets"));
        assertTrue(content.contains("Python: 1 snippets"));
        assertTrue(content.contains("Longest Snippet: Long Snippet"));
        assertTrue(content.contains("Shortest Snippet: Python Snippet"));
    }

    @Test
    void testExportSummaryReport_EmptyComponent() throws IOException {
        SnippetComponent component = new SnippetCollection("Empty Collection");

        SnippetExporter.exportSummaryReport(component, tempFile.toString());

        assertTrue(Files.exists(tempFile));
        String content = Files.readString(tempFile);
        
        assertTrue(content.contains("=== SNIPPET SUMMARY REPORT ==="));
        assertTrue(content.contains("Component: Empty Collection"));
        assertTrue(content.contains("Total Snippets: 0"));
        assertTrue(content.contains("Language Distribution:"));
        assertTrue(content.contains("Longest Snippet: N/A"));
        assertTrue(content.contains("Shortest Snippet: N/A"));
    }

    @Test
    void testExportSummaryReport_NullComponent() {
        assertThrows(IllegalArgumentException.class, () ->
            SnippetExporter.exportSummaryReport(null, tempFile.toString())
        );
    }

    @Test
    void testExportSummaryReport_NullFilename() {
        SnippetComponent component = new SnippetCollection("Test");
        assertThrows(IllegalArgumentException.class, () ->
            SnippetExporter.exportSummaryReport(component, null)
        );
    }

    @Test
    void testExportSummaryReport_EmptyFilename() {
        SnippetComponent component = new SnippetCollection("Test");
        assertThrows(IllegalArgumentException.class, () ->
            SnippetExporter.exportSummaryReport(component, "")
        );
    }
}
