package com.example.thesis.views.utilities;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtility {

    public static final DateTimeFormatter STANDARD_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    public static final DateTimeFormatter HOUR_MINUTE = DateTimeFormatter.ofPattern("HH:mm");

    //static only class
    private DateUtility() {}
}
