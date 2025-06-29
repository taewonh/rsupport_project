package com.taewon.project.rsupport_test.announcement;

import com.taewon.project.rsupport_test.announcement.dto.AnnouncementRegisterDto;
import com.taewon.project.rsupport_test.announcement.dto.AnnouncementViewDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository repository;

    public long register(AnnouncementRegisterDto registerDto) {

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
}
