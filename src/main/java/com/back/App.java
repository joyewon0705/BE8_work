package com.back;

import com.back.domain.wiseSaying.controller.WiseSayingController;
import com.back.domain.wiseSaying.domain.wiseSaying.repository.WiseSayingRepository;
import com.back.domain.wiseSaying.service.WiseSayingService;

import java.util.Scanner;

public class App {
    private static Scanner scanner = null;

    public static void run(Scanner scanner) {
        App.scanner = scanner;
        main(null);
    }

    public static void main(String[] args) {
        WiseSayingRepository repository = new WiseSayingRepository();
        WiseSayingService service = new WiseSayingService(repository);
        WiseSayingController controller = new WiseSayingController(service, scanner);

        if (scanner == null) {
            scanner = new Scanner(System.in);
        }

        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령) ");
            String command = scanner.nextLine().trim();

            if (command.equals("종료")) {
                break;
            } else if (command.equals("등록")) {
                controller.register();
            } else if (command.equals("목록")) {
                controller.list();
            } else if (command.equals("빌드")) {
                controller.build();
            } else if (command.startsWith("삭제?id=")) {
                controller.delete(parseId(command));
            } else if (command.startsWith("수정?id=")) {
                controller.modify(parseId(command));
            }
        }

        System.out.println("프로그램을 종료합니다.");
    }

    private static int parseId(String command) {
        try {
            return Integer.parseInt(command.split("id=")[1]);
        } catch (Exception e) {
            return -1;
        }
    }
}