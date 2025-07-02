package com.taewon.project.rsupport_test.announcement;

import com.taewon.project.rsupport_test.announcement.dto.AnnouncementSummaryDto;
import org.apache.lucene.document.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class AnnouncementConverter {

    public static final class PFN {

        public static final String id                   =   "id";
        public static final String title                =   "title";
        public static final String description                =   "description";
        public static final String author               =   "author";
        public static final String start_expose_time    =   "start_expose_time";
        public static final String end_expose_time      =   "end_expose_time";
        public static final String deleted              =   "deleted";
        public static final String expose               =   "expose";
        public static final String created_at           =   "created_at";
        public static final String updated_at           =   "updated_at";
        public static final String updated_at_sort      =   "update_at_sort";
    }

    public static Document toDocument(Announcement announcement, LocalDateTime now) {

        Document document = new Document();

        // 고유 ID
        document.add(new StringField(PFN.id, announcement.getId().toString(), Field.Store.YES));

        // 검색 대상 필드들
        document.add(new TextField(PFN.title, announcement.getTitle(), Field.Store.YES));
        document.add(new TextField(PFN.description, announcement.getDescription(), Field.Store.YES));
        document.add(new TextField(PFN.author, announcement.getAuthor(), Field.Store.YES));
        document.add(new TextField(PFN.start_expose_time, announcement.getStart_expose_time().toString(), Field.Store.YES));
        document.add(new TextField(PFN.end_expose_time, announcement.getEnd_expose_time().toString(), Field.Store.YES));

        // 상태 관련
        document.add(new StringField(PFN.deleted, Boolean.TRUE.equals(announcement.getDeleted()) ? "T" : "F", Field.Store.YES));
        document.add(new StringField(PFN.expose, Boolean.TRUE.equals(announcement.availableExpose(now)) ? "T" : "F", Field.Store.YES));

        long createdAtMillis = announcement.getCreatedAt().getTime();
        document.add(new StoredField(PFN.created_at, createdAtMillis)); // 검색 결과에 표시

        long updatedAtMillis = announcement.getUpdatedAt().getTime();
        document.add(new StoredField(PFN.updated_at, updatedAtMillis)); // 검색 결과에 표시
        document.add(new NumericDocValuesField(PFN.updated_at_sort, updatedAtMillis)); // 정렬용

        return document;
    }

    public static AnnouncementSummaryDto toSummary(Document document) {

        return AnnouncementSummaryDto.builder()
                .id(Long.parseLong(document.get(PFN.id)))
                .title(document.get(PFN.title))
                .author(document.get(PFN.author))
                .created_at(getTime(document, PFN.created_at))
                .updated_at(getTime(document, PFN.updated_at))
                .build();
    }

    private static LocalDateTime getTime(Document document, String timeField) {

        long createdAtMillis = Long.parseLong(document.get(timeField));
        return Instant.ofEpochMilli(createdAtMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
