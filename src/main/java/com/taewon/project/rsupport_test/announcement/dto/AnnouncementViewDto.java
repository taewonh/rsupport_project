package com.taewon.project.rsupport_test.announcement.dto;

import com.taewon.project.rsupport_test.announcement.Announcement;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Builder
public class AnnouncementViewDto {

    private long id;

    private String title;

    private String description;

    private Timestamp created_at;

    private Timestamp updated_at;

    public static AnnouncementViewDto fromEntity(Announcement announcement) {

        validateExpose(announcement);

        return AnnouncementViewDto.builder()
                .id(announcement.getId())
                .title(announcement.getTitle())
                .description(announcement.getDescription())
                .created_at(announcement.getCreatedAt())
                .updated_at(announcement.getUpdatedAt())
                .build();
    }

    private static void validateExpose(Announcement announcement) {

        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(announcement.getStart_expose_time())) {
            throw new RuntimeException("해당 공지사항의 열람 가능 기간이 시작되지 않았습니다.");
        }

        if (now.isAfter(announcement.getEnd_expose_time())) {
            throw new RuntimeException("해당 공지사항의 열람 가능한 기간이 지났습니다.");
        }
    }
}
