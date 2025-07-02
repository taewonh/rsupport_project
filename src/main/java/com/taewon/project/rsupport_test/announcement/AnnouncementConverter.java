package com.taewon.project.rsupport_test.announcement;

import com.taewon.project.rsupport_test.announcement.dto.AnnouncementSummaryDto;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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
    }

    public static Document toDocument(Announcement announcement, LocalDateTime now) {

        Document document = new Document();

        document.add(new StringField(PFN.id, announcement.getId().toString(), Field.Store.YES));
        document.add(new TextField(PFN.title, announcement.getTitle(), Field.Store.YES));
        document.add(new TextField(PFN.description, announcement.getDescription(), Field.Store.YES));
        document.add(new TextField(PFN.author, announcement.getAuthor(), Field.Store.YES));
        document.add(new TextField(PFN.start_expose_time, announcement.getStart_expose_time().toString(), Field.Store.YES));
        document.add(new TextField(PFN.end_expose_time, announcement.getEnd_expose_time().toString(), Field.Store.YES));
        document.add(new StringField(PFN.deleted, Boolean.TRUE.equals(announcement.getDeleted()) ? "T":"F", Field.Store.YES));
        document.add(new StringField(PFN.expose, Boolean.TRUE.equals(announcement.availableExpose(now)) ? "T":"F", Field.Store.YES));
        document.add(new TextField(PFN.created_at, announcement.getCreatedAt().toString(), Field.Store.YES));
        document.add(new TextField(PFN.updated_at, announcement.getUpdatedAt().toString(), Field.Store.YES));

        return document;
    }

    public static AnnouncementSummaryDto toSummary(Document document) {

        return AnnouncementSummaryDto.builder()
                .id(Long.parseLong(document.get(PFN.id)))
                .title(document.get(PFN.title))
                .author(document.get(PFN.author))
                .created_at(Timestamp.valueOf(document.get(PFN.created_at)))
                .updated_at(Timestamp.valueOf(document.get(PFN.updated_at)))
                .build();
    }
}
