package ru.practicum.explorewithme.service.event.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.service.category.mapper.CategoryMapper;
import ru.practicum.explorewithme.service.category.model.Category;
import ru.practicum.explorewithme.service.comment.dto.CommentDto;
import ru.practicum.explorewithme.service.comment.mapper.CommentMapper;
import ru.practicum.explorewithme.service.event.dto.EventFullDto;
import ru.practicum.explorewithme.service.event.dto.EventShortDto;
import ru.practicum.explorewithme.service.event.dto.NewEventDto;
import ru.practicum.explorewithme.service.event.model.Event;
import ru.practicum.explorewithme.service.location.mapper.LocationMapper;
import ru.practicum.explorewithme.service.location.model.Location;
import ru.practicum.explorewithme.service.user.model.User;

import java.util.Collections;
import java.util.List;

import static ru.practicum.explorewithme.service.user.mapper.UserMapper.toUserShortDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

    public static EventFullDto mapToEventFullDto(Event event) {
        if (event == null) {
            return null;
        }
        List<CommentDto> commentDtoList = Collections.emptyList();
        if (event.getComments() != null) {
            commentDtoList = CommentMapper.mapToCommentDtoList(event.getComments());
        }

        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .location(LocationMapper.mapToLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .title(event.getTitle())
                .state(event.getState())
                .description(event.getDescription())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .createdOn(event.getCreatedOn())
                .initiator(toUserShortDto(event.getInitiator()))
                .requestModeration(event.getRequestModeration())
                .publishedOn(event.getPublishedOn())
                .comments(commentDtoList)
                .build();
    }

    public static EventShortDto mapToEventShortDto(Event event) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }

    public static Event mapToNewEvent(NewEventDto eventDto, Location location, User user, Category category) {
        Event event = new Event();
        event.setAnnotation(eventDto.getAnnotation());
        event.setCategory(category);
        event.setDescription(eventDto.getDescription());
        event.setEventDate(eventDto.getEventDate());
        event.setLocation(location);
        event.setPaid(eventDto.getPaid());
        event.setParticipantLimit(eventDto.getParticipantLimit());
        event.setRequestModeration(eventDto.getRequestModeration());
        event.setTitle(eventDto.getTitle());
        event.setInitiator(user);
        return event;
    }

}