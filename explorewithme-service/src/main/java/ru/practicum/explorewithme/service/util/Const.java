package ru.practicum.explorewithme.service.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class Const {
    public static final String PAGE_FROM = "0";
    public static final String PAGE_SIZE = "10";

    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String START_DATE = LocalDateTime.now().minusDays(1111)
            .format(DateTimeFormatter.ofPattern(DATETIME_PATTERN));
    public static final String END_DATE = LocalDateTime.now().plusDays(1111)
            .format(DateTimeFormatter.ofPattern(DATETIME_PATTERN));

    public static final DateTimeFormatter START_DATE_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_PATTERN);
}