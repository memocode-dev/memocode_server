package dev.memocode.memo_server.domain.memocomment.mapper;

import dev.memocode.memo_server.domain.memocomment.dto.response.CommentsDTO;
import dev.memocode.memo_server.domain.memocomment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentMapper {
    public Page<CommentsDTO> entity_to_commentsDto(Page<Comment> comments) {
        List<CommentsDTO> commentsDTOS = comments.stream()
                .map(CommentsDTO::from)
                .toList();

        return PageableExecutionUtils.getPage(commentsDTOS, comments.getPageable(), comments::getTotalElements);
    }
}