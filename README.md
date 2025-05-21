# SNIPPET ORGANIZER 

## 🧠 Project Objective

Create a Java application that allows users to save, organize, and search code snippets offline, in a fast, secure, and structured way, without the need for a database. Data is saved to files.

## 👤 User Experience (Step-by-step)

The app can be used via console (CLI).

**✅ Phase 1: Application Start**
 
When the user starts the app, a main menu appears:

```shell
--- CODE SNIPPET ORGANIZER ---
1. Add new snippet
2. Search snippets
3. Edit snippet
4. Delete snippet
5. Export snippets
0. Close application
Select an option:
```

**✅ Phase 2: Adding a Snippet**

User selects 1 → App asks:

```shell
Title:
> Bubble Sort Java

Language:
> Java

Enter the code (end with "X" on a new line):
> for (int i = 0; i < arr.length; i++) {
>     ...
> }
> X
```

The snippet is saved in a file, snippets.json, with its category and tags.

**✅ Phase 3: Searching Snippets**
User selects 2 → Can search by:

- Title
- Language
- Code

Example:

```shell
Search snippets by keyword:
> sort
```

App shows:

```shell
Found 2 snippets:
1. Bubble Sort Java [Java] 
2. Merge Sort Python [Python]
```

Shows snippets associated with the selected category.

**✅ Phase 4: Edit / Delete**

Options 4 and 5: User chooses a snippet, modifies or deletes it. The app handles everything by updating the file.

**✅ Phase 5: Export Snippets**

Export all snippets to a single txt file

## 📚 Documentation and Justification

### Project Overview
The Snippet Organizer is designed to be a lightweight, offline-first code snippet management system. It prioritizes simplicity, portability, and ease of use while maintaining robust functionality for code organization.

### Design Decisions

1. **File-based Storage**
   - Uses JSON for data persistence
   - Easy to backup and version control

2. **CLI First Approach**
   - Simple and fast interface
   - Keyboard-driven workflow

### Technical Patterns

1. **Factory Pattern**
   - `SnippetFactory` for creating snippets
   - Encapsulates object creation
   - Makes it easy to modify creation logic

2. **Composite Pattern**
   - `SnippetCollection` for managing snippets
   - Treats individual and collections uniformly
s
3. **Iterator Pattern**
   - `SnippetIterator` for traversing snippets
   - Hides implementation details
   - Consistent access to collections

4. **Exception Shielding Pattern**
   - Custom `SnippetException` class
   - Meaningful error messages
   - Proper error handling

### Core Technologies

1. **Collections Framework**
   - `List` for snippet collections
   - `Set` for tags
   - Custom collections and iterators

2. **Generics**
   - Type-safe collections
   - Reusable components
   - Better code organization

3. **Java I/O**
   - File-based storage
   - JSON serialization
   - Export functionality

4. **Logging**
   - Custom `SnippetLogger`
   - Timestamp tracking
   - Error logging

5. **JUnit Testing**
   - Unit tests for core functionality
   - Test coverage for critical paths
   - Automated testing

6. **Maven for Jackson**
   - JSON serialization
   - Data persistence
   - Easy dependency management

### Secure Programming

1. **Input Sanitization**
   - Validation of all user inputs
   - Prevention of empty/null values
   - Safe file operations

2. **No Hardcoded Secrets**
   - Configuration-based approach
   - Secure file handling
   - No sensitive data exposure

3. **Controlled Exception Propagation**
   - Custom exception handling
   - Meaningful error messages
   - Proper error recovery

## 🚀 Getting Started

1. Clone the repository
2. Build with Maven: `mvn clean install`
3. Run the application: `java -jar target/snippet-organizer.jar`

## 📝 Usage Examples

### Adding a Snippet
```shell
Title: Quick Sort
Language: Java
Code:
public void quickSort(int[] arr) {
    // Implementation
}
X
```

### Searching Snippets
```shell
Search keyword: sort
```

### Exporting Snippets
```shell
Export all snippets to a single file
```

## 🔧 Configuration

The application uses the following configuration:
- `snippets.json`: Main data file
- `snippet_organizer.log`: Log file
- Export directory: User-specified

## 📦 Dependencies

- Jackson for JSON processing
- JUnit for testing
- Maven for build management
