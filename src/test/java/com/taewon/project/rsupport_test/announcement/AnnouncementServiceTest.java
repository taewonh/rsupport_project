package com.taewon.project.rsupport_test.announcement;

import com.taewon.project.rsupport_test.announcement.dto.AnnouncementRegisterRequest;
import com.taewon.project.rsupport_test.announcement.dto.AnnouncementViewDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

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

        AnnouncementRegisterRequest registerDto = AnnouncementTestUtil.createRegisterDtoExpose();
        Announcement announcement = registerDto.toEntity();

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

        AnnouncementRegisterRequest registerDto = AnnouncementTestUtil.createRegisterDtoBeforeExpose();
        testFailedFindAnnouncementView(registerDto, "해당 공지사항의 열람 가능 기간이 시작되지 않았습니다.");
    }

    @Test
    void 조회날짜가_마지막일자_이후일때_공지사항_열람_불가() {

        AnnouncementRegisterRequest registerDto = AnnouncementTestUtil.createRegisterDtoAfterExpose();
        testFailedFindAnnouncementView(registerDto, "해당 공지사항의 열람 가능한 기간이 지났습니다.");
    }

    private void testFailedFindAnnouncementView(AnnouncementRegisterRequest registerDto, String expectedErrorMessage) {

        Announcement announcement = registerDto.toEntity();

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
}
