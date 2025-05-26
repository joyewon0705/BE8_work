package com.back;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Path baseDir = Paths.get("db/wiseSaying");
    private static final Path lastIdPath = baseDir.resolve("lastId.txt");

    private static List<WiseSaying> wiseSayings;
    private static int wiseSayingId;

    public static void main(String[] args) {
        System.out.println("== 명언 앱 ==");

        wiseSayingId = getLastId();
        wiseSayings = getAllWiseSayings();

        while (true) {
            System.out.print("명령) ");
            String command = scanner.nextLine().trim();

            if (command.equals("종료")) {
                break;
            } else if (command.equals("등록")) {
                register();
            } else if (command.equals("목록")) {
                list();
            } else if (command.startsWith("삭제?id=")) {
                delete(parseId(command));
            } else if (command.startsWith("수정?id=")) {
                modify(parseId(command));
            } else if (command.equals("빌드")) {
                buildDataJson();
            }
        }

        System.out.println("프로그램을 종료합니다.");
    }

    private static void register() {
        System.out.print("명언 : ");
        String content = scanner.nextLine().trim();
        System.out.print("작가 : ");
        String author = scanner.nextLine().trim();

        wiseSayingId++;
        WiseSaying ws = new WiseSaying(wiseSayingId, author, content);
        if (ws.save(baseDir)) {
            saveLastId(wiseSayingId);
            wiseSayings.add(ws);
            System.out.println(wiseSayingId + "번 명언이 등록되었습니다.");
        } else {
            wiseSayingId--;
        }
    }

    private static void list() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        for (int i = wiseSayings.size() - 1; i >= 0; i--) {
            WiseSaying ws = wiseSayings.get(i);
            System.out.println(ws.getId() + " / " + ws.getAuthor() + " / " + ws.getContent());
        }
    }

    private static void delete(int id) {
        WiseSaying ws = findById(id);

        if (ws != null) {
            if (ws.delete(baseDir)) {
                wiseSayings.remove(ws);
                System.out.println(id + "번 명언이 삭제되었습니다.");
            }
        } else {
            System.out.println(id + "번 명언은 존재하지 않습니다.");
        }
    }

    private static void modify(int id) {
        WiseSaying ws = findById(id);

        if (ws != null) {
            System.out.println("명언(기존) : " + ws.getContent());
            System.out.print("명언 : ");
            ws.setContent(scanner.nextLine());
            System.out.println("작가(기존) : " + ws.getAuthor());
            System.out.print("작가 : ");
            ws.setAuthor(scanner.nextLine());

            ws.save(baseDir);
        } else {
            System.out.println(id + "번 명언은 존재하지 않습니다.");
        }
    }

    private static void buildDataJson() {
        Path path = baseDir.resolve("data.json");
        StringBuilder sb = new StringBuilder();

        sb.append("[\n");
        for (int i = 0; i < wiseSayings.size(); i++) {
            WiseSaying ws = wiseSayings.get(i);
            sb.append(ws.toJsonString(1));
            if (i < wiseSayings.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");

        try {
            Files.writeString(path, sb.toString());
            System.out.println("data.json 파일의 내용이 갱신되었습니다.");
        } catch (IOException e) {
            System.out.println("data.json 생성 실패: " + e.getMessage());
        }
    }

    private static int parseId(String command) {
        try {
            return Integer.parseInt(command.split("id=")[1]);
        } catch (Exception e) {
            return -1;
        }
    }

    private static WiseSaying findById(int id) {
        for (WiseSaying ws : wiseSayings) {
            if (ws.getId() == id) return ws;
        }
        return null;
    }

    private static int getLastId() {
        try {
            List<String> lines = Files.readAllLines(lastIdPath);
            return Integer.parseInt(lines.get(0));
        } catch (IOException e) {
            System.out.println("id 불러오기 실패: " + e.getMessage());
            return -1;
        }
    }

    private static boolean saveLastId(int id) {
        try {
            Files.write(lastIdPath, String.valueOf(id).getBytes());
            return true;
        } catch (IOException e) {
            System.out.println("id 저장 실패: " + e.getMessage());
            return false;
        }
    }

    private static List<WiseSaying> getAllWiseSayings() {
        List<WiseSaying> wiseSayings = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(baseDir, "*.json")) {
            for (Path path : stream) {
                if (path.getFileName().toString().equals("data.json")) continue;

                WiseSaying ws = new WiseSaying().fromJson(path);
                if (ws != null) {
                    wiseSayings.add(ws);
                }
            }
            wiseSayings.sort(Comparator.comparing(WiseSaying::getId));
            return wiseSayings;
        } catch (IOException e) {
            System.out.println("명언 목록 불러오기 실패: " + e.getMessage());
            return null;
        }
    }
}