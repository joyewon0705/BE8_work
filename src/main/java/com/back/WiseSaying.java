package com.back;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WiseSaying {
    private int id;
    private String author;
    private String content;

    public WiseSaying() {
    }

    public WiseSaying(int id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean save(Path baseDir) {
        Path filePath = baseDir.resolve(id + ".json");
        String json = toJsonString(0);

        try {
            Files.writeString(filePath, json);
            return true;
        } catch (IOException e) {
            System.out.println("명언 저장 실패: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(Path baseDir) {
        Path filePath = baseDir.resolve(id + ".json");

        try {
            Files.deleteIfExists(filePath);
            return true;
        } catch (IOException e) {
            System.out.println("명언 삭제 실패: " + e.getMessage());
            return false;
        }
    }

    public String toJsonString(int indentLevel) {
        String indent = "  ".repeat(indentLevel);
        String innerIndent = "  ".repeat(indentLevel + 1);

        return indent + "{\n" +
                innerIndent + "\"id\": " + id + ",\n" +
                innerIndent + "\"content\": \"" + content + "\",\n" +
                innerIndent + "\"author\": \"" + author + "\"\n" +
                indent + "}";
    }

    public WiseSaying fromJson(Path path) {
        try {
            String json = Files.readString(path);
            int id = Integer.parseInt(JsonUtils.getJsonValue(json, "id"));
            String content = JsonUtils.getJsonValue(json, "content");
            String author = JsonUtils.getJsonValue(json, "author");
            return new WiseSaying(id, author, content);
        } catch (IOException e) {
            System.out.println("명언 불러오기 실패: " + e.getMessage());
            return null;
        }
    }
}
