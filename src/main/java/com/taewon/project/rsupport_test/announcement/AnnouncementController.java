package com.taewon.project.rsupport_test.announcement;

import com.taewon.project.rsupport_test.announcement.dto.AnnouncementRegisterDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "공지사항 API", description = "AnnouncementController")
@RequestMapping(path = "/rest/announcement", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @Operation(summary = "01. 공지사항 등록")
    @PostMapping
    public void registerAnnouncement(@RequestBody AnnouncementRegisterDto registerDto) {

        announcementService.register(registerDto);
    }
}
