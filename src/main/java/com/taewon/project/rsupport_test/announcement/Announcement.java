package com.taewon.project.rsupport_test.announcement;

import lombok.Builder;

import java.sql.Timestamp;

@Builder
public class Announcement {

    private Long id;

    private String title;

    private String description;

    private Timestamp start_at;

    private Timestamp end_at;

    // TODO 첨부파일 목록은 별도 리스트로 관리 필요
}
