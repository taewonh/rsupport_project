package com.taewon.project.rsupport_test.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateUtil {

    public static Long toEpochMillis(String dateStr) {
        if (dateStr == null) {
            return null;
        }
        LocalDateTime dateTime = LocalDate.parse(dateStr).atStartOfDay();
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
