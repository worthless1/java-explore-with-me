package ru.practicum.explorewithme.service.request.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.explorewithme.service.event.model.Event;
import ru.practicum.explorewithme.service.user.model.User;
import ru.practicum.explorewithme.service.event.model.enums.EventRequestStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

import static ru.practicum.explorewithme.service.util.Const.DATETIME_PATTERN;
import static ru.practicum.explorewithme.service.event.model.enums.EventRequestStatus.PENDING;

@Entity
@Table(name = "requests")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@DynamicUpdate
@ToString
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    @ToString.Exclude
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", updatable = false)
    @ToString.Exclude
    private User requester;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventRequestStatus status = PENDING;

    @Column(name = "created_date", updatable = false, nullable = false)
    @DateTimeFormat(pattern = DATETIME_PATTERN)
    private LocalDateTime created = LocalDateTime.now();
}