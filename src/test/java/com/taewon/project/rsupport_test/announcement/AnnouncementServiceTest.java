package com.taewon.project.rsupport_test.announcement;

import com.taewon.project.rsupport_test.announcement.dto.AnnouncementRegisterDto;
import com.taewon.project.rsupport_test.announcement.dto.AnnouncementViewDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // 1.
public class AnnouncementServiceTest {

    @InjectMocks
    AnnouncementService announcementService;

    @Mock
    AnnouncementRepository announcementRepository;

    @Test
    void 조회날짜가_시작_종료일자_사이일때_공지사항_열람() {

        AnnouncementRegisterDto registerDto = createRegisterDto(-1, 1);
        Announcement announcement = Announcement.insertBuilder()
                .title(registerDto.getTitle())
                .description(registerDto.getDescription())
                .start_expose_time(registerDto.getStart_expose_time())
                .end_expose_time(registerDto.getEnd_expose_time())
                .build();

        when(announcementRepository.save(any(Announcement.class))).thenAnswer(invocation -> {
            ReflectionTestUtils.setField(announcement, "id", 1L); // Reflective access
            return announcement;
        });

        long announcementId = announcementService.register(registerDto);

        when(announcementRepository.findById(announcementId)).thenReturn(Optional.of(announcement));

        AnnouncementViewDto announcementView = announcementService.findAnnouncementView(announcementId);

        Assertions.assertNotNull(announcementView);
        Assertions.assertEquals(announcementId, announcementView.getId());
    }

    @Test
    void 조회날짜가_시작일자_이전일때_공지사항_열람_불가() {

        testFailedFindAnnouncementView(1, 2, "해당 공지사항의 열람 가능 기간이 시작되지 않았습니다.");
    }

    @Test
    void 조회날짜가_마지막일자_이후일때_공지사항_열람_불가() {

        testFailedFindAnnouncementView(-2, -1, "해당 공지사항의 열람 가능한 기간이 지났습니다.");
    }

    private void testFailedFindAnnouncementView(long beforeDays, long afterDays, String expectedErrorMessage) {

        AnnouncementRegisterDto registerDto = createRegisterDto(beforeDays, afterDays);
        Announcement announcement = Announcement.insertBuilder()
                .title(registerDto.getTitle())
                .description(registerDto.getDescription())
                .start_expose_time(registerDto.getStart_expose_time())
                .end_expose_time(registerDto.getEnd_expose_time())
                .build();

        when(announcementRepository.save(any(Announcement.class))).thenAnswer(invocation -> {
            ReflectionTestUtils.setField(announcement, "id", 1L); // Reflective access
            return announcement;
        });

        long announcementId = announcementService.register(registerDto);

        when(announcementRepository.findById(announcementId)).thenReturn(Optional.of(announcement));

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            announcementService.findAnnouncementView(announcementId);
        });

        Assertions.assertEquals(
                expectedErrorMessage, exception.getMessage()
        );
    }

    private AnnouncementRegisterDto createRegisterDto(long beforeDays, long afterDays) {

        LocalDateTime now = LocalDateTime.now();
        return new AnnouncementRegisterDto(
                "공지사항 타이틀", "공지사항 내용",
                now.plusDays(beforeDays), now.plusDays(afterDays)
        );
    }
}
