package com.wooteco.sokdak.post.domain;

import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

public class SearchQuery {

    private static final Pattern SPECIAL_CHARS = Pattern.compile("\\[‘”-#@;=*/+]");
    private static final Set<String> STRING_SET = Set.of("update", "select", "delete", "insert");

    private final String value;

    public SearchQuery(String value) {
        this.value = toSafeQuery(value);
    }

    private String toSafeQuery(String query) {
        if (query != null) {
            query = changeNoSqlInjection(query.toLowerCase(Locale.ROOT));
        }
        if (query == null) {
            query = "";
        }
        return query;
    }

    private String changeNoSqlInjection(String query) {
        query = SPECIAL_CHARS.matcher(query).replaceAll("");
        for (String s : STRING_SET) {
            if (query.contains(s)) {
                return "";
            }
        }
        return query;
    }

    public String getValue() {
        return value;
    }
}
