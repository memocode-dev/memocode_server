package dev.memocode.memo_server.usecase;

import dev.memocode.memo_server.domain.memocomment.dto.request.CommentCreateDTO;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
public interface PostCommentUseCase {
    UUID createComments(CommentCreateDTO dto);
}
