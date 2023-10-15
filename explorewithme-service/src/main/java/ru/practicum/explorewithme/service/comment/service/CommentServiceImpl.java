package ru.practicum.explorewithme.service.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.service.comment.dto.CommentDto;
import ru.practicum.explorewithme.service.comment.dto.NewCommentDto;
import ru.practicum.explorewithme.service.comment.mapper.CommentMapper;
import ru.practicum.explorewithme.service.comment.model.Comment;
import ru.practicum.explorewithme.service.comment.repository.CommentRepository;
import ru.practicum.explorewithme.service.event.model.Event;
import ru.practicum.explorewithme.service.event.repository.EventRepository;
import ru.practicum.explorewithme.service.exception.NotFoundException;
import ru.practicum.explorewithme.service.exception.ValidateException;
import ru.practicum.explorewithme.service.user.model.User;
import ru.practicum.explorewithme.service.user.repository.UserRepository;
import ru.practicum.explorewithme.service.util.Pagination;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.explorewithme.service.event.model.enums.EventState.PUBLISHED;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public CommentDto createCommentPrivate(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User user = getUserById(userId);

        Event event = getEventById(eventId);

        Comment comment = CommentMapper.mapToComment(user, event, newCommentDto);

        if (!event.getState().equals(PUBLISHED)) {
            throw new ValidateException("Event hasn't published yet.");
        }
        comment = commentRepository.save(comment);

        log.info("Create comment of user with id= {}", userId);
        return CommentMapper.mapToCommentDto(comment);
    }

    @Transactional
    @Override
    public CommentDto updateCommentByIdPrivate(Long userId, Long commentId, NewCommentDto newCommentDto) {
        Comment comment = getCommentById(commentId);

        if (comment.getAuthor().getId().equals(userId)) {
            comment.setText(newCommentDto.getText());
            comment = commentRepository.save(comment);
            log.info("Update comment of user with id= {}", userId);
            return CommentMapper.mapToCommentDto(comment);
        } else {
            throw new NotFoundException("Comment not found for the user");
        }

    }

    @Override
    public CommentDto getCommentByIdPrivate(Long userId, Long commentId) {
        Comment comment = getCommentById(commentId);

        if (comment.getAuthor().getId().equals(userId)) {
            log.info("Get comment by user with id={}", commentId);
            return CommentMapper.mapToCommentDto(comment);
        } else {
            throw new NotFoundException("Comment not found for the user");
        }
    }

    @Override
    public List<CommentDto> getCommentsByEventIdPrivate(Long userId, Long eventId, Integer from, Integer size) {
        userExistenceCheck(userId);

        Event event = getEventById(eventId);
        Pagination pageable = new Pagination(from, size, Sort.unsorted());

        if (event.getInitiator().getId().equals(userId)) {
            List<Comment> comments = commentRepository.findByEventId(eventId, pageable);
            log.info("Get comment by user with id={} and event with id = {}", userId, eventId);

            return comments.stream()
                    .map(CommentMapper::mapToCommentDto)
                    .collect(Collectors.toList());
        } else {
            throw new NotFoundException("Event not found for the user with id=" + userId);
        }

    }

    @Override
    public List<CommentDto> getCommentsByAuthorIdPrivate(Long userId, Integer from, Integer size) {
        userExistenceCheck(userId);

        Pagination pageable = new Pagination(from, size, Sort.unsorted());

        List<Comment> comments = commentRepository.findByUserId(userId, pageable);
        log.info("Get comments by user with id={}", userId);

        return comments.stream()
                .map(CommentMapper::mapToCommentDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteCommentByIdPrivate(Long userId, Long commentId) {
        userExistenceCheck(userId);

        Comment comment = getCommentById(commentId);

        if (comment.getAuthor().getId().equals(userId)) {
            log.info("Delete comment with id={} of user with id={}", commentId, userId);
            commentRepository.deleteById(commentId);
        } else {
            throw new NotFoundException("Comment not found for the user with id=" + userId);
        }

    }

    @Override
    public CommentDto getCommentByIdAdmin(Long commentId) {
        log.info("Get comment with id= {} on admin", commentId);
        return CommentMapper.mapToCommentDto(getCommentById(commentId));
    }

    @Override
    public CommentDto updateCommentAdmin(Long commentId, NewCommentDto newCommentDto) {
        Comment comment = getCommentById(commentId);
        comment.setText(newCommentDto.getText());
        comment = commentRepository.save(comment);

        log.info("Update comment {} with  id={} on admin part", newCommentDto, commentId);
        return CommentMapper.mapToCommentDto(comment);
    }

    @Override
    public List<CommentDto> searchComments(Long eventId, String text, Integer from, Integer size) {
        Event event = getEventById(eventId);

        Pagination pageable = new Pagination(from, size, Sort.unsorted());

        List<Comment> comments = commentRepository.findByEventAndTextContaining(event, text, pageable);
        log.info("Get comment for event with text= {}", text);

        return comments.stream()
                .map(CommentMapper::mapToCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCommentByIdAdmin(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundException("Comment with id=" + commentId + " not found");
        }
        commentRepository.deleteById(commentId);
        log.info("Delete comment with id=c{} on admin part", commentId);
    }


    private Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " not found"));
    }

    private Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " not found"));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " not found"));
    }

    private void userExistenceCheck(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " not found");
        }
    }

}