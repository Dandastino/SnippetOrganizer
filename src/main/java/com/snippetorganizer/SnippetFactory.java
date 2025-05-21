package com.snippetorganizer;

/*
 * SnippetFactory class provides static methods to create Snippet objects.
 * It can create a Snippet with specified parameters or an empty Snippet.
 */
public class SnippetFactory {
    public static Snippet createSnippet(int id, String title, String language, String code) {
        return new Snippet(id, title, language, code);
    }
}