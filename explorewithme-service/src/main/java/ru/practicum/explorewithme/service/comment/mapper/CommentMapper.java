package ru.practicum.explorewithme.service.comment.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.service.comment.dto.CommentDto;
import ru.practicum.explorewithme.service.comment.dto.NewCommentDto;
import ru.practicum.explorewithme.service.comment.model.Comment;
import ru.practicum.explorewithme.service.event.model.Event;
import ru.practicum.explorewithme.service.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {

    public static CommentDto mapToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .eventId(comment.getEvent().getId())
                .authorId(comment.getAuthor().getId())
                .created(comment.getCreated())
                .build();
    }

    public static Comment mapToComment(User user, Event event, NewCommentDto newCommentDto) {
        Comment comment = new Comment();
        comment.setText(newCommentDto.getText());
        comment.setAuthor(user);
        comment.setEvent(event);
        return comment;
    }

    public static List<CommentDto> mapToCommentDtoList(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::mapToCommentDto)
                .collect(Collectors.toList());
    }

}