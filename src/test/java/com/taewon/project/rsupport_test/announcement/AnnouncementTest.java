package com.taewon.project.rsupport_test.announcement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AnnouncementTest {

    @Test
    public void 공지사항_작성() {

        Announcement announcement = Announcement.builder()
                .title("테스트 공지사항 제목")
                .description("테스트 공지사항 내용")
                .build();

        Assertions.assertNotNull(announcement);
    }
}
