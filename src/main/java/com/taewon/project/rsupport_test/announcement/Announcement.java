package com.taewon.project.rsupport_test.announcement;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity(name = "announcement")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private Boolean deleted;

    @Column(name = "start_expose_time")
    private LocalDateTime start_expose_time;

    @Column(name = "end_expose_time")
    private LocalDateTime end_expose_time;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @Column(name = "author")
    private String author;

    // TODO 첨부파일 목록은 별도 리스트로 관리 필요

    @Builder(builderClassName = "InsertBuilder", builderMethodName = "insertBuilder")
    public Announcement(String title,
                        String description,
                        LocalDateTime start_expose_time,
                        LocalDateTime end_expose_time,
                        String author) {
        this.title = title;
        this.description = description;
        this.start_expose_time = start_expose_time;
        this.end_expose_time = end_expose_time;
        this.deleted = false;
        this.author = author;
    }

    public void delete() {
        this.deleted = true;
        this.deletedAt = new Timestamp(System.currentTimeMillis());
    }

    @PrePersist
    void createdAt() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

    public boolean availableExpose(LocalDateTime now) {

        if (now.isBefore(start_expose_time)) {
            return false;
        }

        if (now.isAfter(end_expose_time)) {
            return false;
        }

        return true;
    }

    public void update(Announcement announcement) {

        this.title = announcement.getTitle();
        this.description = announcement.getDescription();
        this.start_expose_time = announcement.getStart_expose_time();
        this.end_expose_time = announcement.getEnd_expose_time();
    }
}
