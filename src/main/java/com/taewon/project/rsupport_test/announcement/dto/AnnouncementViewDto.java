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

    public static AnnouncementViewDto fromEntity(Announcement announcement, boolean validateExpose) {

        if (validateExpose) {
            validateExpose(announcement);
        }

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

        if (!announcement.availableExpose(now)) {
            throw new RuntimeException("선택하신 공지사항의 열람 가능 기간이 아닙니다.");
        }
    }
}
