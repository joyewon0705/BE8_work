package com.back;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<WiseSaying> wiseSayings = new ArrayList<>();
    private static int wiseSayingId = 1;

    public static void main(String[] args) {
        System.out.println("== 명언 앱 ==");

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
            }
        }

        System.out.println("프로그램을 종료합니다.");
    }

    private static void register() {
        System.out.print("명언 : ");
        String content = scanner.nextLine().trim();
        System.out.print("작가 : ");
        String author = scanner.nextLine().trim();

        wiseSayings.add(new WiseSaying(wiseSayingId, author, content));

        System.out.println(wiseSayingId++ + "번 명언이 등록되었습니다.");
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
            wiseSayings.remove(ws);
            System.out.println(id + "번 명언이 삭제되었습니다.");
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
        } else {
            System.out.println(id + "번 명언은 존재하지 않습니다.");
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
}
