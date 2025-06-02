package com.back;

import com.back.standard.util.TestUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class AppTest {
    private static final Path baseDir = Paths.get("db/wiseSaying");

    public static void clear() {
        try {
            if (Files.exists(baseDir)) {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(baseDir)) {
                    for (Path path : stream) {
                        if (!path.getFileName().toString().equals("lastId.txt")) {
                            Files.delete(path);
                        }
                    }
                }
            } else {
                Files.createDirectories(baseDir);
            }

            // lastId.txt를 0으로 초기화
            Path lastIdPath = baseDir.resolve("lastId.txt");
            Files.writeString(lastIdPath, "0");

        } catch (IOException e) {
            throw new RuntimeException("테스트 초기화 실패: ", e);
        }
    }

    public static String run(String input) {
        Scanner scanner = TestUtil.genScanner(input + "종료\n");
        ByteArrayOutputStream byteArrayOutputStream = TestUtil.setOutToByteArray();

        App.run(scanner);

        String out = byteArrayOutputStream.toString().trim();
        TestUtil.clearSetOutToByteArray(byteArrayOutputStream);
        return out;
    }
}