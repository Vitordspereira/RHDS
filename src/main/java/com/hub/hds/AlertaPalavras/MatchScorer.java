package com.hub.hds.AlertaPalavras;

import java.util.Set;

public final class MatchScorer {

    private MatchScorer() {}

    public static int score(Set<String> alertKeys, Set<String> vagaKeys) {
        int score = 0;

        for (String a : alertKeys) {
            for (String v : vagaKeys) {

                if (v.equals(a)) {
                    score += 3; // match forte
                } else if (v.contains(a) || a.contains(v)) {
                    score += 2; // match parcial
                }
            }
        }
        return score;
    }
}

