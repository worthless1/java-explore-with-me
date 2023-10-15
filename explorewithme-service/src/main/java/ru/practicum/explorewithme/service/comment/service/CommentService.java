package ru.practicum.explorewithme.service.comment.service;


import ru.practicum.explorewithme.service.comment.dto.CommentDto;
import ru.practicum.explorewithme.service.comment.dto.NewCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createCommentPrivate(Long userId, Long eventId, NewCommentDto newCommentDto);

    CommentDto updateCommentByIdPrivate(Long userId, Long commentId, NewCommentDto newCommentDto);

    CommentDto getCommentByIdPrivate(Long userId, Long commentId);

    List<CommentDto> getCommentsByAuthorIdPrivate(Long userId, Integer from, Integer size);

    List<CommentDto> getCommentsByEventIdPrivate(Long userId, Long eventId, Integer from, Integer size);

    void deleteCommentByIdPrivate(Long userId, Long commentId);

    CommentDto getCommentByIdAdmin(Long commentId);

    CommentDto updateCommentAdmin(Long commentId, NewCommentDto newCommentDto);

    void deleteCommentByIdAdmin(Long commentId);

    List<CommentDto> searchComments(Long eventId, String text, Integer from, Integer size);

}