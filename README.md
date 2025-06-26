# SNIPPET ORGANIZER 

## 🧠 Project Objective

Create a Java application that allows users to save, organize, and search code snippets offline, in a fast, secure, and structured way, without the need for a database. Data is saved to files in a dedicated `data/` directory.

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
6. Analyze snippets
7. Manage tags
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

Description (optional):
> Efficient sorting algorithm with O(n²) complexity

Tags (comma-separated, optional):
> sorting, algorithm, java, bubble-sort

Enter the code (end with "X" on a new line):
> for (int i = 0; i < arr.length; i++) {
>     ...
> }
> X
```

The snippet is saved in a file, `data/snippets.json`, with its metadata, tags, and description.

**✅ Phase 3: Searching Snippets**
User selects 2 → Can search by:

- **Keyword Search**: Search across title, code, language, tags, and description
- **Tag Search**: Find snippets by specific tags

Example:

```shell
Search options:
1. Search by keyword
2. Search by tag
Select search type: 1

Enter search keyword: sort
```

App shows:

```shell
Found snippet:
ID: 1
Title: Bubble Sort Java
Language: Java
Description: Efficient sorting algorithm with O(n²) complexity
Tags: sorting, algorithm, java, bubble-sort
Code:
for (int i = 0; i < arr.length; i++) {
    ...
}
---------------------------
```

**✅ Phase 4: Edit / Delete**

Options 3 and 4: User chooses a snippet, modifies or deletes it. The app handles everything by updating the file.

**✅ Phase 5: Export Snippets**

Export all snippets to a single txt file with detailed formatting including tags and descriptions. Files are saved in the `data/` directory.

**✅ Phase 6: Analyze Snippets**

Comprehensive analysis including:
- Language distribution
- Tag distribution and statistics
- Description statistics
- Code length analysis
- Longest/shortest snippets

**✅ Phase 7: Manage Tags**

- View all tags in the collection
- Add tags to existing snippets
- Remove tags from snippets
- Search snippets by specific tags

## 📊 UML Class Diagram

```mermaid
classDiagram
    %% Main Application
    class App {
        +main(String[] args) void
        -getNonEmptyInput(Scanner, String) String
        -getValidIntInput(Scanner, String) int
    }

    %% Core Business Logic
    class SnippetManager {
        -FILE_NAME: String
        -DATA_DIR: String
        -file: File
        -objectMapper: ObjectMapper
        -snippetComponent: SnippetComponent
        +SnippetManager()
        +addSnippet(String, String, String) void
        +addSnippet(String, String, String, Set~String~, String) void
        +searchSnippets(String) void
        +searchByTag(String) void
        +getAllTags() Set~String~
        +editSnippet(int, String, String, String) void
        +deleteSnippet(int) void
        +getAllSnippets() List~Snippet~
        +getSnippetComponent() SnippetComponent
        +getSnippetCount() int
        -loadSnippets() void
        -saveSnippets() void
        -getNextId() int
    }

    %% Composite Pattern Interface
    class SnippetComponent {
        <<interface>>
        +getName() String
        +getAllSnippets() List~Snippet~
        +addSnippet(Snippet) void
        +removeSnippet(Snippet) void
        +getSnippetCount() int
        +isEmpty() boolean
        +display() void
    }

    %% Composite Pattern Implementations
    class Snippet {
        -id: int
        -title: String
        -language: String
        -code: String
        -tags: Set~String~
        -description: String
        +Snippet(int, String, String, String)
        +Snippet(int, String, String, String, Set~String~, String)
        +getId() int
        +setId(int) void
        +getTitle() String
        +setTitle(String) void
        +getLanguage() String
        +setLanguage(String) void
        +getCode() String
        +setCode(String) void
        +getTags() Set~String~
        +setTags(Set~String~) void
        +addTag(String) void
        +removeTag(String) void
        +hasTag(String) boolean
        +getDescription() String
        +setDescription(String) void
        +toString() String
    }

    class SnippetCollection {
        -snippets: List~Snippet~
        -name: String
        +SnippetCollection(String)
        +addSnippet(Snippet) void
        +removeSnippet(Snippet) void
        +getAllSnippets() List~Snippet~
        +getName() String
        +setName(String) void
        +getSnippetCount() int
        +isEmpty() boolean
        +display() void
    }

    %% Factory Pattern
    class SnippetFactory {
        <<static>>
        +createSnippet(int, String, String, String) Snippet
    }

    %% Iterator Pattern
    class SnippetIterator {
        -snippets: List~Snippet~
        -position: int
        +SnippetIterator(List~Snippet~)
        +hasNext() boolean
        +next() Snippet
    }

    %% Exception Handling
    class SnippetException {
        +SnippetException(String)
        +SnippetException(String, Throwable)
    }

    %% Logging
    class SnippetLogger {
        <<static>>
        -LOG_FILE: String
        -DATA_DIR: String
        -formatter: DateTimeFormatter
        +log(String) void
        +logError(String, Throwable) void
        +logInfo(String) void
    }

    %% Analysis Features
    class SnippetAnalyzer {
        <<static>>
        +analyzeComponent(SnippetComponent) Map~String, Object~
        +getLanguageDistribution(SnippetComponent) Map~String, Integer~
        +getTagDistribution(SnippetComponent) Map~String, Integer~
        +getAverageCodeLength(SnippetComponent) double
        +getLongestSnippet(SnippetComponent) Snippet
        +getShortestSnippet(SnippetComponent) Snippet
        +displayAnalysis(SnippetComponent) void
        +displayEnhancedAnalysis(SnippetComponent) void
        +findSnippetsByLanguage(SnippetComponent, String) List~Snippet~
        +getSnippetsWithCodeLongerThan(SnippetComponent, int) List~Snippet~
        +getSnippetsWithDescriptions(SnippetComponent) List~Snippet~
        +getSnippetsWithoutDescriptions(SnippetComponent) List~Snippet~
    }

    %% Export Features
    class SnippetExporter {
        <<static>>
        -DATA_DIR: String
        +exportToText(List~Snippet~, String) void
        +exportComponentToText(SnippetComponent, String) void
        +exportByLanguage(SnippetComponent, String) void
        +exportSummaryReport(SnippetComponent, String) void
    }

    %% Relationships
    App --> SnippetManager : uses
    App --> SnippetAnalyzer : uses
    App --> SnippetExporter : uses
    
    SnippetManager --> SnippetComponent : manages
    SnippetManager --> SnippetFactory : creates snippets
    SnippetManager --> SnippetIterator : iterates
    SnippetManager --> SnippetException : throws
    SnippetManager --> SnippetLogger : logs
    
    SnippetComponent <|.. Snippet : implements (leaf)
    SnippetComponent <|.. SnippetCollection : implements (composite)
    
    SnippetCollection --> Snippet : contains
    
    SnippetFactory --> Snippet : creates
    
    SnippetIterator --> Snippet : iterates over
    
    SnippetAnalyzer --> SnippetComponent : analyzes
    SnippetAnalyzer --> Snippet : works with
    
    SnippetExporter --> SnippetComponent : exports
    SnippetExporter --> Snippet : exports
    SnippetExporter --> SnippetAnalyzer : uses for reports
    SnippetExporter --> SnippetLogger : logs exports
```

## 📚 Documentation and Justification

### Project Overview
The Snippet Organizer is designed to be a lightweight, offline-first code snippet management system. It prioritizes simplicity, portability, and ease of use while maintaining robust functionality for code organization.

### Design Decisions

1. **File-based Storage**
   - Uses JSON for data persistence
   - All files stored in dedicated `data/` directory
   - Easy to backup and version control

2. **CLI First Approach**
   - Simple and fast interface
   - Keyboard-driven workflow

3. **Enhanced Organization**
   - Tags system for better categorization
   - Descriptions for better documentation
   - Multiple search options for easy retrieval

### Technical Patterns

1. **Factory Pattern**
   - `SnippetFactory` for creating snippets
   - Encapsulates object creation
   - Makes it easy to modify creation logic

2. **Composite Pattern**
   - `SnippetComponent` interface for uniform treatment
   - `Snippet` as leaf nodes and `SnippetCollection` as composite nodes
   - Allows treating individual snippets and collections uniformly
   - Enables hierarchical organization of code snippets

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
   - `Set` for tags (prevents duplicates)
   - Custom collections and iterators

2. **Generics**
   - Type-safe collections
   - Reusable components
   - Better code organization

3. **Java I/O**
   - File-based storage in `data/` directory
   - JSON serialization
   - Export functionality

4. **Logging System**
   - Custom `SnippetLogger` class
   - Timestamp tracking for all operations
   - Automatic log rotation (deletes logs > 1MB)
   - Error and info logging
   - Log file: `data/snippet_organizer.log`

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

1. Clone the repository `git clone https://github.com/Dandastino/SnippetOrganizer.git`
2. Change the directory `cd SnippetOrganizer`
3. Build with Maven: `mvn clean install`
4. Run the application: `java -jar target/demo-1.0-SNAPSHOT.jar`

## 📁 Project Structure

```
SnippetOrganizer/
├── src/
│   ├── main/java/com/snippetorganizer/
│   │   ├── App.java
│   │   ├── SnippetManager.java
│   │   ├── Snippet.java
│   │   ├── SnippetCollection.java
│   │   ├── SnippetComponent.java
│   │   ├── SnippetFactory.java
│   │   ├── SnippetIterator.java
│   │   ├── SnippetException.java
│   │   ├── SnippetLogger.java
│   │   ├── SnippetAnalyzer.java
│   │   └── SnippetExporter.java
│   └── test/java/
│       ├── SnippetManagerTest.java
│       ├── SnippetTest.java
│       ├── SnippetCollectionTest.java
│       ├── SnippetIteratorTest.java
│       ├── SnippetLoggerTest.java
│       ├── SnippetAnalyzerTest.java
│       └── SnippetExporterTest.java
├── target/                        
├── pom.xml                        
├── .gitignore                     
└── README.md                      
data/                           
   ├── snippets.json              
   ├── snippet_organizer.log      
   └── exported_*.txt             
```

## 📝 Usage Examples

### Adding a Snippet with Tags and Description
```shell
Title: Quick Sort Algorithm
Language: Java
Description: Efficient sorting algorithm with O(n log n) average case complexity
Tags: sorting, algorithm, java, divide-and-conquer, recursive
Code:
public void quickSort(int[] arr, int low, int high) {
    if (low < high) {
        int pi = partition(arr, low, high);
        quickSort(arr, low, pi - 1);
        quickSort(arr, pi + 1, high);
    }
}
X
```

### Searching Snippets
```shell
# Keyword search
Search keyword: sort

# Tag search
Search by tag: algorithm
```

### Managing Tags
```shell
# View all tags
Manage tags → View all tags in collection

# Search by tag
Manage tags → View snippets by tag → Enter: java
```

### Enhanced Analysis
```shell
# Get comprehensive statistics
Analyze snippets → Enhanced analysis with tag distribution
```

**Export Function Details:**
- Exports all snippets to a single text file
- Each snippet includes ID, title, language, description, tags, and full code
- Formatted with clear separators between snippets
- Useful for backup, sharing, or documentation purposes
- Files are saved in the `data/` directory

**Logging System Details:**
- All operations are logged with timestamps
- Log file: `data/snippet_organizer.log`
- Automatic log rotation (deletes old logs > 1MB)
- Tracks: snippet additions, edits, deletions, exports, and errors
- Helps with debugging and audit trails

**Tag System Benefits:**
- **Better Organization**: Categorize snippets by topic, language, or use case
- **Easy Retrieval**: Find specific code patterns quickly
- **Learning Aid**: Tag snippets by difficulty or concept
- **Team Sharing**: Export snippets by tags for specific projects

**✅ Gif Tutorial**


## 🔧 Configuration

The application uses the following configuration:
- `data/snippets.json`: Main data file containing all snippets with tags and descriptions
- `data/snippet_organizer.log`: Log file with operation history
- Export directory: `data/` (all exported files are saved here)

## 📦 Dependencies

- Jackson for JSON processing
- JUnit for testing
- Maven for build management

