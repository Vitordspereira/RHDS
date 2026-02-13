package com.hub.hds.AlertaPalavras;

import java.text.Normalizer;
import java.util.Locale;

public class TextUtils {
    private TextUtils (){}

    public static String normalize(String text) {
        if (text == null) return "";

        String n = Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase(Locale.ROOT);

        return n.replaceAll("[^a-z0-9 ]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }
}
