package ru.practicum.explorewithme.server.util;

import java.time.format.DateTimeFormatter;

public final class Const {
    private Const() {
    }

    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

}