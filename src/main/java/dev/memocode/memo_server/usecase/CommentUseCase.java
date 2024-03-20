package dev.memocode.memo_server.usecase;

import dev.memocode.memo_server.domain.memocomment.dto.request.ChildCommentCreateDTO;
import dev.memocode.memo_server.domain.memocomment.dto.request.CommentCreateDTO;
import dev.memocode.memo_server.domain.memocomment.dto.request.CommentDeleteDto;
import dev.memocode.memo_server.domain.memocomment.dto.request.CommentUpdateDTO;
import dev.memocode.memo_server.domain.memocomment.dto.response.CommentsDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
public interface CommentUseCase {
    UUID createComments(@Valid CommentCreateDTO dto);

    void updateComments(@Valid CommentUpdateDTO dto);

    void deleteComments(CommentDeleteDto dto);

    Page<CommentsDTO> findAllComments(UUID memoId, int page, int size);

    UUID createChildComment(ChildCommentCreateDTO dto);
}
