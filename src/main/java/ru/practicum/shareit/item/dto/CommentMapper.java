package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.dto.UserMapper;

import java.time.Instant;
import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, ItemMapper.class},
        imports = Instant.class)
public interface CommentMapper {
    @Mapping(target = "authorName", expression = "java(comment.getAuthor().getName())")
    @Mapping(target = "created", expression = "java(Instant.now())")
    CommentDto toCommentDto(Comment comment);

    List<CommentDto> toCommentDto(List<Comment> comments);
}


