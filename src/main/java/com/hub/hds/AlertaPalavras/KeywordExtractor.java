package com.hub.hds.AlertaPalavras;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class KeywordExtractor {
    private KeywordExtractor() {}

    private static final Set<String> STOPWORDS = Set.of(
            "de", "da", "do", "em", "para", "com", "e", "ou", "um", "uma"
    );

    public static Set<String> extract(String text) {
        String n = TextUtils.normalize(text);
        if (n.isBlank()) return Set.of();

        return Arrays.stream(n.split(" "))
                .filter(w -> w.length() >= 3)
                .filter(w -> !STOPWORDS.contains(w))
                .collect(Collectors.toSet());
    }
}
