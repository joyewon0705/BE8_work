package com.back.domain.wiseSaying.repository;

import com.back.util.JsonUtils;
import com.back.domain.wiseSaying.entity.WiseSaying;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class WiseSayingRepository {
    private final Path baseDir = Paths.get("db/wiseSaying");
    private final Path lastIdPath = baseDir.resolve("lastId.txt");

    public int getLastId() {
        try {
            if (Files.exists(lastIdPath)) {
                return Integer.parseInt(Files.readString(lastIdPath).trim());
            }
        } catch (IOException e) {
            System.out.println("ERROR: lastId 불러오기 실패: " + e.getMessage());
        }
        return 0;
    }

    public void saveLastId(int id) {
        try {
            Files.writeString(lastIdPath, String.valueOf(id));
        } catch (IOException e) {
            System.out.println("ERROR: lastId 저장 실패: " + e.getMessage());
        }
    }

    public void save(WiseSaying ws) {
        Path path = baseDir.resolve(ws.getId() + ".json");
        try {
            Files.writeString(path, JsonUtils.toJsonString(ws, 0));
        } catch (IOException e) {
            System.out.println("ERROR: 명언 저장 실패: " + e.getMessage());
        }
    }

    public void delete(WiseSaying ws) {
        Path path = baseDir.resolve(ws.getId() + ".json");
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            System.out.println("ERROR: 명언 삭제 실패: " + e.getMessage());
        }
    }

    public List<WiseSaying> findAll() {
        List<WiseSaying> list = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(baseDir, "*.json")) {
            for (Path path : stream) {
                if (path.getFileName().toString().equals("data.json")) continue;

                WiseSaying ws = JsonUtils.fromJson(path);
                if (ws != null) {
                    list.add(ws);
                }
            }
            list.sort(Comparator.comparing(WiseSaying::getId));
        } catch (IOException e) {
            System.out.println("ERROR: 명언 목록 불러오기 실패: " + e.getMessage());
        }
        return list;
    }

    public Optional<WiseSaying> findById(int id) {
        Path path = baseDir.resolve(id + ".json");
        if (!Files.exists(path)) return Optional.empty();
        return Optional.ofNullable(JsonUtils.fromJson(path));
    }

    public void exportAllToJsonFile() {
        Path path = baseDir.resolve("data.json");
        List<WiseSaying> list = findAll();
        String json = JsonUtils.toJsonArrayString(list);

        try {
            Files.writeString(path, json);
        } catch (IOException e) {
            System.out.println("ERROR: data.json 생성 실패: " + e.getMessage());
        }
    }
}
