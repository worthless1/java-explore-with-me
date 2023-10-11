package ru.practicum.explorewithme.service.event.model.enums;

import java.util.Arrays;
import java.util.Optional;

public enum EventsSort {

    EVENT_DATE, VIEWS;

    public static Optional<EventsSort> from(String stringState) {
        return Arrays.stream(values())
                .filter(state -> state.name().equalsIgnoreCase(stringState))
                .findFirst();
    }
}
