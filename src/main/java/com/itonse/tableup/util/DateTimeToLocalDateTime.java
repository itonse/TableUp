package com.itonse.tableup.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeToLocalDateTime {

    public static LocalDateTime from(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(dateTime, formatter);
    }
}
