package ru.practicum.explorewithme.service.compilation.model;

import lombok.*;
import ru.practicum.explorewithme.service.event.model.Event;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "compilations")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(nullable = false)
    private Boolean pinned;
    @ManyToMany
    @JoinTable(name = "compilation_events", joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    @ToString.Exclude
    private Set<Event> events;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Compilation that = (Compilation) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getId());
    }
}