package hexlet.code;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class SearchEngineTest {

    //    @Test
    void findDocumentTest13Step() {
        var doc1 = "I can't shoot straight unless I've had a pint!";
        var doc2 = "Don't shoot shoot shoot that thing at me.";
        var doc3 = "I'm your shooter.";

        var docs = List.of(
            Map.of("id", "doc1", "text", doc1),
            Map.of("id", "doc2", "text", doc2),
            Map.of("id", "doc3", "text", doc3)
        );

        Assertions.assertIterableEquals(List.of("doc1", "doc2"), SearchEngine.search(docs, "shoot"));
        Assertions.assertIterableEquals(List.of(), SearchEngine.search(new ArrayList<>(), "shoot"));
    }

    //    @Test
    void findDocumentTest2Step() {
        var doc1 = "I can't shoot straight unless I've had a pint!";
        var docs = List.of(Map.of("id", "doc1", "text", doc1));

        Assertions.assertIterableEquals(List.of("doc1"), SearchEngine.search(docs, "pint"));
        Assertions.assertIterableEquals(List.of("doc1"), SearchEngine.search(docs, "pint!"));
    }

    @Test
    void findDocumentTest456Step() {
        var doc1 = "I can't shoot straight unless I've had a pint!";
        var doc2 = "Don't shoot shoot shoot that thing at me.";
        var doc3 = "I'm your shooter.";
        var doc4 = "Don't shoot shoot that thing at me.";

        List<Map<String, String>> docs = List.of(
            Map.of("id", "doc1", "text", doc1),
            Map.of("id", "doc2", "text", doc2),
            Map.of("id", "doc3", "text", doc3),
            Map.of("id", "doc4", "text", doc4)
        );

        Assertions.assertIterableEquals(List.of("doc2", "doc4", "doc1"), SearchEngine.search(docs, "shoot at me"));
    }
}
