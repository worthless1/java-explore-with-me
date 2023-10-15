package ru.practicum.explorewithme.service.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explorewithme.service.comment.model.Comment;
import ru.practicum.explorewithme.service.event.model.Event;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.event.id = :eventId")
    List<Comment> findByEventId(@Param("eventId") Long eventId, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.author.id = :userId")
    List<Comment> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.event = :event AND c.text LIKE %:text%")
    List<Comment> findByEventAndTextContaining(@Param("event") Event event, @Param("text") String text, Pageable pageable);
}