package com.taewon.project.rsupport_test.announcement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taewon.project.rsupport_test.announcement.Announcement;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class AnnouncementRegisterDto {

    @NotEmpty(message = "announcement title must not be empty")
    private String title;

    @NotEmpty(message = "announcement description must not be empty")
    private String description;

    @NotNull(message = "announcement start expose time must not be null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime start_expose_time;

    @NotNull(message = "announcement end expose time must not be null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime end_expose_time;

    public Announcement toEntity() {

        return Announcement.insertBuilder()
                .title(title)
                .description(description)
                .start_expose_time(start_expose_time)
                .end_expose_time(end_expose_time)
                .build();
    }
}
