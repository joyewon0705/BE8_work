package com.back.domain.wiseSaying.service;

import com.back.domain.wiseSaying.repository.WiseSayingRepository;
import com.back.domain.wiseSaying.entity.WiseSaying;

import java.util.List;
import java.util.Optional;

public class WiseSayingService {
    private final WiseSayingRepository repository;

    public WiseSayingService(WiseSayingRepository repository) {
        this.repository = repository;
    }

    public WiseSaying register(String author, String content) {
        int id = repository.getLastId() + 1;
        WiseSaying ws = new WiseSaying(id, author, content);
        repository.save(ws);
        repository.saveLastId(id);
        return ws;
    }

    public List<WiseSaying> findAll() {
        return repository.findAll();
    }

    public boolean deleteById(int id) {
        Optional<WiseSaying> optional = repository.findById(id);
        if (optional.isEmpty()) return false;

        repository.delete(optional.get());
        return true;
    }

    public boolean modify(int id, String newAuthor, String newContent) {
        Optional<WiseSaying> optional = repository.findById(id);
        if (optional.isEmpty()) return false;

        WiseSaying ws = optional.get();
        ws.setAuthor(newAuthor);
        ws.setContent(newContent);
        repository.save(ws);
        return true;
    }

    public Optional<WiseSaying> findById(int id) {
        return repository.findById(id);
    }

    public void buildJsonFile() {
        repository.exportAllToJsonFile();
    }
}
