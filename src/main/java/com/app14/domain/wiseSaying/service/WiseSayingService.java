package com.app14.domain.wiseSaying.service;

import com.app14.domain.wiseSaying.entity.WiseSaying;
import com.app14.domain.wiseSaying.repository.WiseSayingRepository;

import java.util.List;
import java.util.Optional;

public class WiseSayingService {
    private final WiseSayingRepository repository;

    public WiseSayingService(WiseSayingRepository repository) {
        this.repository = repository;
    }

    public void initSampleIfEmpty() {
        if (repository.getLastId() == 0) {
            for (int i = 1; i <= 10; i++) {
                register("명언 " + i, "작자미상 " + i);
            }
        }
    }

    public WiseSaying register(String content, String author) {
        int id = repository.getLastId() + 1;
        WiseSaying ws = new WiseSaying(id, content, author);
        repository.saveLastId(id);
        repository.save(ws);
        return ws;
    }

    public boolean deleteById(int id) {
        Optional<WiseSaying> optional = repository.findById(id);
        if (optional.isEmpty()) return false;

        repository.delete(optional.get());
        return true;
    }

    public Optional<WiseSaying> findbyId(int id) {
        return repository.findById(id);
    }

    public void modify(int id, String content, String author) {
        Optional<WiseSaying> optional = repository.findById(id);
        if (optional.isEmpty()) return;

        WiseSaying ws = optional.get();
        ws.setContent(content);
        ws.setAuthor(author);
        repository.save(ws);
    }

    public void buildJsonFile() {
        repository.saveAll();
    }

    public List<WiseSaying> searchByKeyword(String keywordType, String keyword) {
        if (keywordType.equals("all") || keyword.isBlank()) return repository.findAll();

        return switch (keywordType) {
            case "content" -> repository.findByContent(keyword);
            case "author" -> repository.findByAuthor(keyword);
            default -> List.of();
        };
    }
}
