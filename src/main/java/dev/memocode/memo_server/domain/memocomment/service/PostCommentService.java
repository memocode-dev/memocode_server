package dev.memocode.memo_server.domain.memocomment.service;

import dev.memocode.memo_server.domain.author.entity.Author;
import dev.memocode.memo_server.domain.author.service.AuthorService;
import dev.memocode.memo_server.domain.base.exception.GlobalErrorCode;
import dev.memocode.memo_server.domain.base.exception.GlobalException;
import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memo.service.InternalMemoService;
import dev.memocode.memo_server.domain.memocomment.dto.request.CommentCreateDTO;
import dev.memocode.memo_server.domain.memocomment.entity.Comment;
import dev.memocode.memo_server.domain.memocomment.repository.PostCommentRepository;
import dev.memocode.memo_server.usecase.PostCommentUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static dev.memocode.memo_server.domain.base.exception.GlobalErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostCommentService implements PostCommentUseCase {

    private final InternalMemoService internalMemoService;
    private final AuthorService authorService;
    private final PostCommentRepository postCommentRepository;

    @Override
    @Transactional
    public UUID createComments(CommentCreateDTO dto) {

        Memo memo = internalMemoService.findByMemoIdElseThrow(dto.getMemoId());
        Author author = authorService.findByIdElseThrow(dto.getAuthorId());

        validPost(memo);

        Comment comment = Comment.builder()
                .content(dto.getContent())
                .author(author)
                .memo(memo)
                .build();

        Comment saveComment = postCommentRepository.save(comment);

        return saveComment.getId();
    }

    // 게시글이 삭제되었는지 또는 게시판에 올라가있는지 체크
    private static void validPost(Memo memo) {
        if (memo.getDeleted()){
            throw new GlobalException(MEMO_NOT_FOUND);
        }

        if (!memo.getVisibility()){
            throw new GlobalException(POST_NOT_FOUND);
        }
    }
}
