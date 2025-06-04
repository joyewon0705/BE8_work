package com.app13.domain.wiseSaying.service;

import com.app13.domain.wiseSaying.entity.WiseSaying;
import com.app13.domain.wiseSaying.repository.WiseSayingRepository;

import java.util.List;
import java.util.Optional;

public class WiseSayingService {
    private final WiseSayingRepository repository;

    public WiseSayingService(WiseSayingRepository repository) {
        this.repository = repository;
    }

    public WiseSaying register(String content, String author) {
        int id = repository.getLastId() + 1;
        WiseSaying ws = new WiseSaying(id, content, author);
        repository.saveLastId(id);
        repository.save(ws);
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
}
