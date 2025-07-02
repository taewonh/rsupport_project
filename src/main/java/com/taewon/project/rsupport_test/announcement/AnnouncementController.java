package com.taewon.project.rsupport_test.announcement;

import com.taewon.project.rsupport_test.announcement.dto.AnnouncementListRequest;
import com.taewon.project.rsupport_test.announcement.dto.AnnouncementRegisterRequest;
import com.taewon.project.rsupport_test.announcement.dto.AnnouncementSummaryDto;
import com.taewon.project.rsupport_test.announcement.dto.AnnouncementViewDto;
import com.taewon.project.rsupport_test.common.dto.ListResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "공지사항 API", description = "AnnouncementController")
@RequestMapping(path = "/rest/announcement", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @Operation(summary = "공지사항 등록")
    @PostMapping
    public AnnouncementViewDto registerAnnouncement(@RequestBody AnnouncementRegisterRequest request) {

        return announcementService.register(request);
    }

    @Operation(summary = "공지사항 목록 조회")
    @PostMapping(path = "/_search")
    public ListResult<AnnouncementSummaryDto> fetchAnnouncements(@RequestBody AnnouncementListRequest request) {

        return announcementService.list(request);
    }

    @Operation(summary = "공지사항 상세 조회")
    @GetMapping(path = "/{id}")
    public AnnouncementViewDto detailAnnouncement(@PathVariable Long id) {

        return announcementService.detail(id);
    }

    @Operation(summary = "공지사항 삭제")
    @DeleteMapping(path = "{id}")
    public void deleteAnnouncement(@PathVariable Long id) {

        announcementService.delete(id);
    }

    @Operation(summary = "공지사항 수정")
    @PutMapping(path = "{id}")
    public AnnouncementViewDto updateAnnouncement(@PathVariable Long id,
                                                  @RequestBody AnnouncementRegisterRequest request) {

        return announcementService.update(id, request);
    }
}
