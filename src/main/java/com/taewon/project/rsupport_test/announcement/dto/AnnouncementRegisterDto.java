package com.taewon.project.rsupport_test.announcement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taewon.project.rsupport_test.announcement.Announcement;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class AnnouncementRegisterDto {

    @Schema(description = "공지사항 제목")
    @NotEmpty(message = "announcement title must not be empty")
    private String title;

    @Schema(description = "공지사항 내용")
    @NotEmpty(message = "announcement description must not be empty")
    private String description;

    @Schema(description = "공지사항 열람 허용 시작 시각")
    @NotNull(message = "announcement start expose time must not be null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime start_expose_time;

    @Schema(description = "공지사항 열람 허용 종료 시각")
    @NotNull(message = "announcement end expose time must not be null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime end_expose_time;

    @Schema(description = "작성자 이름")
    @NotEmpty(message = "announcement author must not be null")
    private String author;

    public Announcement toEntity() {
        return Announcement.insertBuilder()
                .title(title)
                .description(description)
                .start_expose_time(start_expose_time)
                .end_expose_time(end_expose_time)
                .author(author)
                .build();
    }
}
