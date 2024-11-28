package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserMapper;

import java.time.Instant;
import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, ItemMapper.class},
        imports = Instant.class)
public interface CommentMapper {
    @Mapping(target = "authorName", expression = "java(comment.getAuthor().getName())")
    CommentDto toCommentDto(Comment comment);

    @Mapping(target = "author", source = "user")
    @Mapping(target = "id", source = "commentDto.id")
    Comment toComment(CommentDto commentDto, User user, Item item);

    List<CommentDto> toCommentDto(List<Comment> comments);
}


