package com.taewon.project.rsupport_test.announcement.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class AnnouncementSummaryDto {

    private long id;

    private String title;

    private String author;

    private boolean expose;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;
}
