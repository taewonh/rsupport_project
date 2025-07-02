package com.taewon.project.rsupport_test.announcement;

import com.taewon.project.rsupport_test.announcement.dto.AnnouncementListRequest;
import com.taewon.project.rsupport_test.announcement.dto.AnnouncementRegisterRequest;
import com.taewon.project.rsupport_test.announcement.dto.AnnouncementSummaryDto;
import com.taewon.project.rsupport_test.announcement.dto.AnnouncementViewDto;
import com.taewon.project.rsupport_test.common.dto.ListResult;
import com.taewon.project.rsupport_test.common.lucene.SearchCacheResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository repository;

    private final AnnouncementCacheService announcementCacheService;

    @Transactional
    public AnnouncementViewDto register(AnnouncementRegisterRequest registerDto) {

        Announcement saved = null;
        try {
            saved = registerDto.toEntity();
            repository.save(saved);
            return AnnouncementViewDto.fromEntity(saved, false);
        } finally {
            if (saved != null) {
                announcementCacheService.writeToIndex(saved);
            }
        }
    }

    public AnnouncementViewDto detail(long announcementId) {

        Announcement announcement = repository.findById(announcementId)
                .orElseThrow(() -> new RuntimeException("Failed to select announcement."));

        if (announcement.getDeleted()) {
            throw new RuntimeException("Failed detail announcement has been already deleted.");
        }

        return AnnouncementViewDto.fromEntity(announcement, true);
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

    @Transactional
    public void delete(Long announcementId) {

        Announcement announcement = repository.findById(announcementId)
                .orElseThrow(() -> new RuntimeException("Failed to select announcement."));

        if (announcement.getDeleted()) {
            throw new RuntimeException("Failed delete announcement has already been deleted");
        }

        announcement.delete();
        announcementCacheService.deleteToIndex(announcement);
    }

    @Transactional
    public AnnouncementViewDto update(Long announcementId, AnnouncementRegisterRequest request) {

        Announcement announcement = repository.findById(announcementId)
                .orElseThrow(() -> new RuntimeException("Failed to select announcement."));

        if (announcement.getDeleted()) {
            throw new RuntimeException("Failed update announcement has been already deleted.");
        }
        announcement.update(request.toEntity());
        announcementCacheService.deleteToIndex(announcement);
        announcementCacheService.writeToIndex(announcement);

        return AnnouncementViewDto.fromEntity(announcement, false);
    }
}
