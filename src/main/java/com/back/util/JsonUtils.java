package com.back.util;

import com.back.domain.wiseSaying.entity.WiseSaying;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class JsonUtils {

    public static String toJsonString(WiseSaying ws, int indentLevel) {
        String indent = "  ".repeat(indentLevel);
        String innerIndent = "  ".repeat(indentLevel + 1);

        return indent + "{\n" +
                innerIndent + "\"id\": " + ws.getId() + ",\n" +
                innerIndent + "\"content\": \"" + ws.getContent() + "\",\n" +
                innerIndent + "\"author\": \"" + ws.getAuthor() + "\"\n" +
                indent + "}";
    }

    public static String toJsonArrayString(List<WiseSaying> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < list.size(); i++) {
            sb.append(toJsonString(list.get(i), 1));
            if (i < list.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");
        return sb.toString();
    }

    public static WiseSaying fromJson(Path path) {
        try {
            String json = Files.readString(path);
            int id = Integer.parseInt(JsonUtils.getJsonValue(json, "id"));
            String content = JsonUtils.getJsonValue(json, "content");
            String author = JsonUtils.getJsonValue(json, "author");
            return new WiseSaying(id, author, content);
        } catch (IOException e) {
            System.out.println("ERROR: JSON 파싱 실패: " + e.getMessage());
            return null;
        }
    }

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
