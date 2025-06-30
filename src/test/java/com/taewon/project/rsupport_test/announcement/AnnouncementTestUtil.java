package com.taewon.project.rsupport_test.announcement;

import com.taewon.project.rsupport_test.announcement.dto.AnnouncementRegisterRequest;

import java.time.LocalDateTime;

public class AnnouncementTestUtil {

    /*
    * 조회 가능 기간 이전의 공지사항 등록 DTO 생성
    * */
    public static AnnouncementRegisterRequest createRegisterDtoBeforeExpose() {
        return createRegisterDto(1, 2);
    }

    /*
     * 조회 가능 기간 이후의 공지사항 등록 DTO 생성
     * */
    public static AnnouncementRegisterRequest createRegisterDtoAfterExpose() {
        return createRegisterDto(-2, -1);
    }

    /*
    * 조회 가능 기간에 속하는 공지사항 등록 DTO 생성
    * */
    public static AnnouncementRegisterRequest createRegisterDtoExpose() {
        return createRegisterDto(-1, 1);
    }

    public static AnnouncementRegisterRequest createRegisterDto(long beforeDays, long afterDays) {

        LocalDateTime now = LocalDateTime.now();
        return new AnnouncementRegisterRequest(
                "공지사항 타이틀", "공지사항 내용",
                now.plusDays(beforeDays), now.plusDays(afterDays), "홍태원"
        );
    }
}
