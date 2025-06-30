package com.taewon.project.rsupport_test.announcement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AnnouncementListRequest {

    @Schema(description="출력 오프셋", defaultValue = "0")
    public Integer offset = 0;

    @Schema(description="출력 건수", defaultValue = "20")
    public Integer count = 20;
}
