package com.taewon.project.rsupport_test.announcement;

import com.taewon.project.rsupport_test.announcement.dto.AnnouncementListRequest;
import com.taewon.project.rsupport_test.announcement.dto.AnnouncementRegisterRequest;
import com.taewon.project.rsupport_test.announcement.dto.AnnouncementSummaryDto;
import com.taewon.project.rsupport_test.common.dto.ListResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SpringBootTest
public class AnnouncementControllerTest {

    @Autowired
    private AnnouncementController announcementController;

    @Autowired
    private AnnouncementRepository announcementRepository;

    private static final int exposeDtoCount = 100;
    private static final int afterExposeDtoCount = 100;
    private static final int beforeExposeDtoCount = 100;

    @BeforeEach
    void init() {

        List<AnnouncementRegisterRequest> registerDtoList = Stream.of(
                        generateRegisterDtoList(exposeDtoCount, AnnouncementTestUtil::createRegisterDtoExpose),
                        generateRegisterDtoList(afterExposeDtoCount, AnnouncementTestUtil::createRegisterDtoAfterExpose),
                        generateRegisterDtoList(beforeExposeDtoCount, AnnouncementTestUtil::createRegisterDtoBeforeExpose)
                ).flatMap(Collection::stream)
                .collect(Collectors.toList());

        Collections.shuffle(registerDtoList);
        registerDtoList.forEach(registerDto -> announcementController.registerAnnouncement(registerDto));
    }

    private List<AnnouncementRegisterRequest> generateRegisterDtoList(int totalCount, Supplier<AnnouncementRegisterRequest> dtoSupplier) {

        return IntStream.range(0, totalCount)
                .mapToObj(current -> dtoSupplier.get())
                .toList();
    }

    @Test
    public void 공지사항_목록_조회() {

        AnnouncementListRequest request = AnnouncementListRequest.builder()
                .build();

        ListResult<AnnouncementSummaryDto> result = announcementController.fetchAnnouncements(request);
    }
}
