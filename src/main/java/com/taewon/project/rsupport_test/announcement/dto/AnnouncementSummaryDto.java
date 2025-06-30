package com.taewon.project.rsupport_test.announcement.dto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Builder
@Getter
public class AnnouncementSummaryDto {

    private long id;

    private String title;

    private String author;

    private Timestamp created_at;

    private Timestamp updated_at;
}
