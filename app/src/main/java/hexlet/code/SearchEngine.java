package hexlet.code;

import java.util.*;

public class SearchEngine {
    private static final String NON_WORD_REGEX_PATTERN = "\\W+";

    private SearchEngine() {
    }

    public static List<String> search(List<Map<String, String>> docs, String searchValue) {
        if (searchValue == null || searchValue.isEmpty() || docs == null || docs.isEmpty()) {
            return Collections.emptyList();
        }

        return docs.stream()
            .filter(document -> document.get("text") != null)
            .map(document ->
                new TempDocument(
                    document.get("id"),
                    document.get("text"),
                    binarySearch(document.get("text").split(" "), searchValue.split(" "))
                )
            )
            .filter(temp -> temp.getSearchResult().getOccurrences() > 0)
            .sorted((o1, o2) -> {
                SearchResult searchResult1 = o1.getSearchResult();
                SearchResult searchResult2 = o2.getSearchResult();
                int compare = Boolean.compare(searchResult2.isFoundAll(), searchResult1.isFoundAll());
                if (compare != 0) {
                    return compare;
                }

                return Integer.compare(searchResult2.getOccurrences(), searchResult1.getOccurrences());
            })
            .map(temp -> temp.toDocument().get("id"))
            .toList();
    }

    private static SearchResult binarySearch(String[] strArray, String[] searchValueArray) {
        String[] processedArray = Arrays.stream(strArray)
            .map(SearchEngine::processString)
            .sorted()
            .toArray(String[]::new);
        String[] processedSearchValueArray = Arrays.stream(searchValueArray)
            .map(SearchEngine::processString)
            .toArray(String[]::new);

        int occurrences = 0;
        Set<String> foundWords = new HashSet<>();
        for (String searchValue : processedSearchValueArray) {
            int result = Arrays.binarySearch(processedArray, searchValue);
            if (result > 0) {
                occurrences += 1;
                foundWords.add(searchValue);
            }
        }
        return new SearchResult(occurrences, foundWords.size() == searchValueArray.length);
    }

    private static String processString(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }

        return s.replaceAll(NON_WORD_REGEX_PATTERN, "");
    }

}
