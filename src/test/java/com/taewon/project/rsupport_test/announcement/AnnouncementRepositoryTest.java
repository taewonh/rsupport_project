package com.taewon.project.rsupport_test.announcement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
public class AnnouncementRepositoryTest {

    @Autowired
    private AnnouncementRepository repository;

    @Test
    public void 공지사항_저장() {

        Announcement announcement = Announcement.insertBuilder()
                .title("테스트 공지사항 제목")
                .description("테스트 공지사항 내용")
                .start_expose_time(LocalDateTime.now())
                .end_expose_time(LocalDateTime.now())
                .build();
        Announcement registered = repository.save(announcement);

        Long id = registered.getId();
        Assertions.assertEquals(1L, id);

        List<Announcement> announcements = repository.findAll();
        Assertions.assertEquals(1, announcements.size());
    }
}
