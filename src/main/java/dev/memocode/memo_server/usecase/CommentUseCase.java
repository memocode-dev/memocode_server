package dev.memocode.memo_server.usecase;

import dev.memocode.memo_server.domain.memocomment.dto.request.CommentCreateDTO;
import dev.memocode.memo_server.domain.memocomment.dto.request.CommentUpdateDTO;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
public interface CommentUseCase {
    UUID createComments(@Valid CommentCreateDTO dto);

    void updateComments(@Valid CommentUpdateDTO dto);
}
