package com.taewon.project.rsupport_test.announcement;

import com.taewon.project.rsupport_test.common.lucene.LuceneUpdatableIndex;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class AnnouncementCacheService {

    private final AnnouncementRepository announcementRepository;

    private LuceneUpdatableIndex announcementIndex;

    public AnnouncementCacheService(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    @PostConstruct
    void init() {
        createAnnouncementsIndex();
    }

    private void createAnnouncementsIndex() {

        List<Announcement> announcements = announcementRepository.findAll();
        announcementIndex = new LuceneUpdatableIndex();

        announcements.forEach(announcement -> {
            Document document = AnnouncementConverter.toDocument(announcement);
            try {
                announcementIndex.write(document);
            } catch (IOException e) {
                log.error("Failed announcement document write to index.", e);
                throw new RuntimeException(e);
            }
        });
    }
}
