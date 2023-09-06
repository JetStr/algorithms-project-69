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

        //make reverse index
        Map<String, List<String>> reversedIndex = buildReversedIndex(docs);

        //search for values
        List<String> processedSearchValues = Arrays.stream(searchValue.split(" "))
            .map(SearchEngine::processString)
            .toList();
        List<String> foundIds = new ArrayList<>();
        for (String processedSearchValue : processedSearchValues) {
            List<String> documentIds = reversedIndex.get(processedSearchValue);
            if (documentIds != null) {
                foundIds.addAll(documentIds);
            }
        }

        //count occurrences of document ids
        Map<String, Integer> map = new HashMap<>();
        for (String s : foundIds) {
            if (map.containsKey(s)) {
                map.put(s, map.get(s) + 1);
            } else {
                map.put(s, 1);
            }
        }

        //desc sort and return
        List<Map.Entry<String, Integer>> entries = map.entrySet().stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .toList();
        return entries.stream().map(Map.Entry::getKey).toList();
    }

    private static Map<String, List<String>> buildReversedIndex(List<Map<String, String>> docs) {
         Map<String, List<String>> reversedIndex = new HashMap<>();

        for (Map<String, String> doc : docs) {
            String id = doc.get("id");
            String text = doc.get("text");
            if (text == null || text.isEmpty()) {
                continue;
            }

            List<String> processedDocument = Arrays.stream(text.split(" "))
                .map(SearchEngine::processString)
                .sorted()
                .toList();

            for (String s : processedDocument) {
                List<String> docIds = reversedIndex.get(s);
                if (docIds == null) {
                    List<String> value = new ArrayList<>();
                    value.add(id);
                    reversedIndex.put(s, value);
                } else {
                    docIds.add(id);
                }
            }
        }

        return reversedIndex;
    }

    private static String processString(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }

        return s.replaceAll(NON_WORD_REGEX_PATTERN, "");
    }

}
