package hexlet.code;

import java.util.Map;

public class TempDocument {

    private final String id;
    private final String text;
    private final SearchResult searchResult;

    public TempDocument(String id, String text, SearchResult searchResult) {
        this.id = id;
        this.text = text;
        this.searchResult = searchResult;
    }

    public SearchResult getSearchResult() {
        return searchResult;
    }

    public Map<String, String> toDocument() {
        return Map.of("id", id, "text", text);
    }
}
