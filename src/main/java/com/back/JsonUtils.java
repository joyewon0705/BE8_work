package com.back;

public class JsonUtils {

    public static String getJsonValue(String json, String key) {
        String target = "\"" + key + "\"";
        int index = json.indexOf(target);
        if (index == -1) return "";

        int start = json.indexOf(':', index) + 1;
        while (start < json.length() && (json.charAt(start) == ' ' || json.charAt(start) == '\"')) {
            start++;
        }

        int end = start;
        boolean isString = json.charAt(start - 1) == '\"';
        while (end < json.length() && (isString ? json.charAt(end) != '\"' : Character.isDigit(json.charAt(end)))) {
            end++;
        }

        return json.substring(start, end).trim();
    }
}
