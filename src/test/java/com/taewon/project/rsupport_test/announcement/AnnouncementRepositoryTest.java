package com.taewon.project.rsupport_test.announcement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
public class AnnouncementRepositoryTest {

    @Autowired
    private AnnouncementRepository repository;

    @Test
    public void 공지사항_저장() {

        Announcement announcement = AnnouncementTestUtil.createRegisterDtoAvailableExpose().toEntity();
        Announcement registered = repository.save(announcement);

        Long id = registered.getId();
        Assertions.assertEquals(1L, id);

        List<Announcement> announcements = repository.findAll();
        Assertions.assertEquals(1, announcements.size());
    }
}
