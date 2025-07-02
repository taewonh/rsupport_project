package com.taewon.project.rsupport_test.announcement;

import com.taewon.project.rsupport_test.announcement.dto.AnnouncementListRequest;
import com.taewon.project.rsupport_test.announcement.dto.AnnouncementRegisterRequest;
import com.taewon.project.rsupport_test.announcement.dto.AnnouncementSummaryDto;
import com.taewon.project.rsupport_test.announcement.dto.AnnouncementViewDto;
import com.taewon.project.rsupport_test.common.dto.ListResult;
import com.taewon.project.rsupport_test.common.lucene.SearchCacheResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository repository;

    private final AnnouncementCacheService announcementCacheService;

    public long register(AnnouncementRegisterRequest registerDto) {

        // TODO register 한 공지사항 데이터 캐시 refresh
        Announcement announcement = registerDto.toEntity();
        return repository.save(announcement).getId();
    }

    public AnnouncementViewDto findAnnouncementView(long announcementId) {

        Announcement announcement = repository.findById(announcementId)
                .orElseThrow(() -> new RuntimeException("Failed to select announcement."));

        return AnnouncementViewDto.fromEntity(announcement);
    }

    public ListResult<AnnouncementSummaryDto> list(AnnouncementListRequest request) {

        SearchCacheResult searchResult = announcementCacheService.search(request);
        if (searchResult.getDocuments() == null) {
            return ListResult.empty();
        }

        List<AnnouncementSummaryDto> announcementSummaries = searchResult.getDocuments()
                .stream()
                .map(AnnouncementConverter::toSummary)
                .toList();

        return new ListResult<>(searchResult.getTotalCount(), request.getOffset(), announcementSummaries);
    }
}
