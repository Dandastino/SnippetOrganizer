package com.snippetorganizer;

import java.util.HashSet;
import java.util.Set;
import com.snippetorganizer.core.Snippet;

public class TestDataUtil {
    public static Set<String> javaTags() {
        Set<String> tags = new HashSet<>();
        tags.add("java");
        tags.add("oop");
        return tags;
    }

    public static Set<String> pythonTags() {
        Set<String> tags = new HashSet<>();
        tags.add("python");
        tags.add("scripting");
        return tags;
    }

    public static Set<String> jsTags() {
        Set<String> tags = new HashSet<>();
        tags.add("javascript");
        tags.add("web");
        return tags;
    }

    public static Snippet javaSnippet(int id) {
        return new Snippet(id, "Java Class", "Java", "public class Test {}", javaTags(), "A Java class");
    }

    public static Snippet pythonSnippet(int id) {
        return new Snippet(id, "Python Function", "Python", "def test(): pass", pythonTags(), "A Python function");
    }

    public static Snippet jsSnippet(int id) {
        return new Snippet(id, "JS Function", "JavaScript", "function test() {}", jsTags(), "A JavaScript function");
    }

    public static Snippet longSnippet(int id) {
        return new Snippet(id, "Long Code", "Java", "public class VeryLongClass { public void veryLongMethod() { System.out.println(\"This is a very long method with lots of code and multiple lines\"); } }", new HashSet<>(), "A very long snippet");
    }

    public static Snippet shortSnippet(int id) {
        return new Snippet(id, "Short Code", "Python", "x=1", new HashSet<>(), "A very short snippet");
    }
} 