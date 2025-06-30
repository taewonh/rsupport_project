package com.taewon.project.rsupport_test.announcement;

import com.taewon.project.rsupport_test.announcement.dto.AnnouncementRegisterRequest;
import com.taewon.project.rsupport_test.announcement.dto.AnnouncementSummaryDto;
import com.taewon.project.rsupport_test.announcement.dto.AnnouncementViewDto;
import com.taewon.project.rsupport_test.common.dto.ListResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@AllArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository repository;

    public long register(AnnouncementRegisterRequest registerDto) {

        // TODO register 한 공지사항 데이터 캐시 refresh
        Announcement announcement = registerDto.toEntity();
        return repository.save(announcement).getId();
    }

    public AnnouncementViewDto findAnnouncementView(long announcementId) {

        // TODO repository select 하지말고 캐시에서 조회
        Announcement announcement = repository.findById(announcementId)
                .orElseThrow(() -> new RuntimeException("Failed to select announcement."));

        return AnnouncementViewDto.fromEntity(announcement);
    }

    public ListResult<AnnouncementSummaryDto> list() {

        List<Announcement> announcements = repository.findAll();
        if (CollectionUtils.isEmpty(announcements)) {
            return ListResult.empty();
        }

        List<AnnouncementSummaryDto> dtoList = announcements.stream()
                .map(announcement -> AnnouncementSummaryDto.builder()
                        .id(announcement.getId())
                        .title(announcement.getTitle())
                        .author(announcement.getAuthor())
                        .created_at(announcement.getCreatedAt())
                        .updated_at(announcement.getUpdatedAt())
                        .build())
                .toList();

        return ListResult.of(dtoList);
    }
}
