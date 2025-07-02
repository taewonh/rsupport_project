package com.taewon.project.rsupport_test.announcement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class AnnouncementListRequest {

    @Schema(description="출력 오프셋", defaultValue = "0")
    public Integer offset = 0;

    @Schema(description="출력 건수", defaultValue = "20")
    public Integer count = 20;

    @Schema(description="검색어")
    public String keyword;

    @Schema(description="검색 시작 일자")
    public LocalDateTime startDate;

    @Schema(description="검색 종료 일자")
    public LocalDateTime endDate;
}
