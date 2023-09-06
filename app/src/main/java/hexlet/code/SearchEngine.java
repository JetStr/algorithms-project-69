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
        Map<String, List<WordStats>> reversedIndex = buildReversedIndex(docs);

        //search for values
        List<String> processedSearchValues = Arrays.stream(searchValue.split(" "))
            .map(SearchEngine::processString)
            .toList();
        List<WordStats> ids = new ArrayList<>();
        for (String processedSearchValue : processedSearchValues) {
            List<WordStats> wordStats = reversedIndex.get(processedSearchValue);
            if (wordStats != null) {
                ids.addAll(wordStats);
            }
        }

        //count weight of document ids
        Map<String, Double> map = new HashMap<>();
        for (WordStats stat : ids) {
            String key = stat.documentId();
            if (map.containsKey(key)) {
                map.put(key, map.get(key) + stat.weight());
            } else {
                map.put(key, stat.weight());
            }
        }

        //desc sort and return
        List<Map.Entry<String, Double>> entries = map.entrySet().stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .toList();
        return entries.stream().map(Map.Entry::getKey).toList();
    }

    private static Map<String, List<WordStats>> buildReversedIndex(List<Map<String, String>> docs) {
         Map<String, List<WordStats>> reversedIndex = new HashMap<>();

         Map<String, List<String>> processedDocs = new HashMap<>();

        for (Map<String, String> doc : docs) {
            processedDocs.put(doc.get("id"), Arrays.stream(doc.get("text").split(" "))
                .map(SearchEngine::processString)
                .sorted()
                .toList());
        }

        List<List<String>> dataSet = processedDocs.values().stream().toList();

        for (Map.Entry<String, List<String>> stringListEntry : processedDocs.entrySet()) {
            String id = stringListEntry.getKey();
            List<String> text = stringListEntry.getValue();

            for (String s : text) {
                List<WordStats> wordStats = reversedIndex.get(s);
                if (wordStats == null) {
                    List<WordStats> value = new ArrayList<>();
                    value.add(new WordStats(id, tf(text, s) * idf(dataSet, s)));
                    reversedIndex.put(s, value);
                } else {
                    WordStats wordStats1 = wordStats.stream()
                        .filter(item -> id.equals(item.documentId()))
                        .findFirst()
                        .orElse(null);
                    if (wordStats1 == null) {
                        wordStats.add(new WordStats(id, tf(text, s) * idf(dataSet, s)));
                    }
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

    private static double tf(List<String> doc, String term) {
        double result = 0;
        for (String word : doc) {
            if (term.equalsIgnoreCase(word))
                result++;
        }
        return result / doc.size();
    }

    private static double idf(List<List<String>> docs, String term) {
        double n = 0;
        for (List<String> doc : docs) {
            for (String word : doc) {
                if (term.equalsIgnoreCase(word)) {
                    n++;
                    break;
                }
            }
        }

        if (n == 0) {
            return 0;
        }

        return Math.log(docs.size() / n);
    }

}
