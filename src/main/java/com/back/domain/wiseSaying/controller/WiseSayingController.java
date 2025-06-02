package com.back.domain.wiseSaying.controller;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.service.WiseSayingService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class WiseSayingController {
    private final WiseSayingService service;
    private final Scanner scanner;

    public WiseSayingController(WiseSayingService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    public void register() {
        System.out.print("명언 : ");
        String content = scanner.nextLine().trim();
        System.out.print("작가 : ");
        String author = scanner.nextLine().trim();

        WiseSaying ws = service.register(author, content);
        System.out.println(ws.getId() + "번 명언이 등록되었습니다.");
    }

    public void list() {
        List<WiseSaying> list = service.findAll();
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");
        for (int i = list.size() - 1; i >= 0; i--) {
            WiseSaying ws = list.get(i);
            System.out.println(ws.getId() + " / " + ws.getAuthor() + " / " + ws.getContent());
        }
    }

    public void delete(int id) {
        if (service.deleteById(id)) {
            System.out.println(id + "번 명언이 삭제되었습니다.");
        } else {
            System.out.println(id + "번 명언은 존재하지 않습니다.");
        }
    }

    public void modify(int id) {
        Optional<WiseSaying> optional = service.findById(id);
        if (optional.isEmpty()) {
            System.out.println(id + "번 명언은 존재하지 않습니다.");
            return;
        }

        WiseSaying ws = optional.get();
        System.out.println("명언(기존) : " + ws.getContent());
        System.out.print("명언 : ");
        String content = scanner.nextLine().trim();
        System.out.println("작가(기존) : " + ws.getAuthor());
        System.out.print("작가 : ");
        String author = scanner.nextLine().trim();

        service.modify(id, author, content);
    }

    public void build() {
        service.buildJsonFile();
        System.out.println("data.json 파일의 내용이 갱신되었습니다.");
    }
}
