package dev.memocode.memo_server.domain.memocomment.service;

import dev.memocode.memo_server.domain.base.exception.GlobalException;
import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memocomment.entity.Comment;
import dev.memocode.memo_server.domain.memocomment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static dev.memocode.memo_server.domain.base.exception.GlobalErrorCode.*;

@Service
@RequiredArgsConstructor
public class InternalCommentService {

    private final CommentRepository commentRepository;

    // 게시글 검증
    void validPost(Memo memo) {
        if (memo.getDeleted()){
            throw new GlobalException(MEMO_NOT_FOUND);
        }

        if (!memo.getVisibility()){
            throw new GlobalException(POST_NOT_FOUND);
        }
    }

    public Comment findByCommentId(UUID commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new GlobalException(POST_COMMENT_NOT_FOUND));
    }

    public void validOwner(UUID ownerId, UUID authorId) {
        if (!ownerId.equals(authorId)){
            throw new GlobalException(NOT_VALID_POST_COMMENT_OWNER);
        }
    }
}
