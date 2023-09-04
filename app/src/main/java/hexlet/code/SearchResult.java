package hexlet.code;

public class SearchResult {

    private final int occurrences;

    private final boolean foundAll;

    public SearchResult(int occurrences, boolean foundAll) {
        this.occurrences = occurrences;
        this.foundAll = foundAll;
    }

    public int getOccurrences() {
        return occurrences;
    }

    public boolean isFoundAll() {
        return foundAll;
    }
}
