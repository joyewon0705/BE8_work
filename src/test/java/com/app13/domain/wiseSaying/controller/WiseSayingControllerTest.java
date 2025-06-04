package com.app13.domain.wiseSaying.controller;

import com.app13.AppTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class WiseSayingControllerTest {

    final String commandRegister1 = """
                등록
                현재를 사랑하라.
                작자미상
            """;

    final String commandRegister2 = """
                등록
                과거에 집착하지 마라.
                작자미상
            """;

    @BeforeEach
    void beforeEach() {
        AppTest.clear();
    }

    @Test
    @DisplayName("등록")
    void t3() {
        final String out = AppTest.run(
                commandRegister1
        );

        assertThat(out)
                .contains("명언 :")
                .contains("작가 :")
                .contains("1번 명언이 등록되었습니다.");
    }


    @Test
    @DisplayName("등록: 명언번호 증가")
    void t4() {
        final String out = AppTest.run(
                commandRegister1 + commandRegister2
        );

        assertThat(out)
                .contains("1번 명언이 등록되었습니다.")
                .contains("2번 명언이 등록되었습니다.");
    }

    @Test
    @DisplayName("목록")
    void t5() {
        final String out = AppTest.run(
                commandRegister1 + commandRegister2 + "목록\n"
        );

        assertThat(out)
                .contains("번호 / 작가 / 명언")
                .contains("----------------------")
                .contains("2 / 작자미상 / 과거에 집착하지 마라.")
                .contains("1 / 작자미상 / 현재를 사랑하라.");
    }

    @Test
    @DisplayName("삭제")
    void t6() {
        final String out = AppTest.run(
                commandRegister1 + "삭제?id=1\n"
        );

        assertThat(out)
                .contains("1번 명언이 삭제되었습니다.");
    }

    @Test
    @DisplayName("삭제: 존재하지 않는 명언 삭제")
    void t7() {
        final String out = AppTest.run(
                commandRegister1 + "삭제?id=1\n" + "삭제?id=1\n"
        );

        assertThat(out)
                .contains("1번 명언이 삭제되었습니다.")
                .contains("1번 명언은 존재하지 않습니다.");
    }

    @Test
    @DisplayName("수정")
    void t8() {
        final String out = AppTest.run(
                commandRegister1 + commandRegister2 + """
                        수정?id=2
                        현재와 자신을 사랑하라.
                        홍길동
                        목록
                        """
        );

        assertThat(out)
                .contains("명언(기존) : 과거에 집착하지 마라.")
                .contains("작가(기존) : 작자미상")
                .contains("2 / 홍길동 / 현재와 자신을 사랑하라.")
                .doesNotContain("2 / 작자미상 / 과거에 집착하지 마라.");
    }

    @Test
    @DisplayName("빌드")
    void t9() throws IOException {
        final String out = com.back.AppTest.run(
                commandRegister1 + commandRegister2 + "빌드\n"
        );

        assertThat(out)
                .contains("data.json 파일의 내용이 갱신되었습니다.");

        Path path = Paths.get("db/wiseSaying/data.json");
        assertThat(Files.exists(path)).isTrue();
        assertThat(Files.readString(path).trim()).isEqualTo("""
                [
                  {
                    "id": 1,
                    "content": "현재를 사랑하라.",
                    "author": "작자미상"
                  },
                  {
                    "id": 2,
                    "content": "과거에 집착하지 마라.",
                    "author": "작자미상"
                  }
                ]
                """.trim());
    }

    @Test
    @DisplayName("목록")
    void t10() {

    }
}