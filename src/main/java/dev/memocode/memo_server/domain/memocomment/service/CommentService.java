package dev.memocode.memo_server.domain.memocomment.service;

import dev.memocode.memo_server.domain.author.entity.Author;
import dev.memocode.memo_server.domain.author.service.AuthorService;
import dev.memocode.memo_server.domain.base.exception.GlobalException;
import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memo.service.InternalMemoService;
import dev.memocode.memo_server.domain.memocomment.dto.request.CommentCreateDTO;
import dev.memocode.memo_server.domain.memocomment.dto.request.CommentUpdateDTO;
import dev.memocode.memo_server.domain.memocomment.entity.Comment;
import dev.memocode.memo_server.domain.memocomment.repository.CommentRepository;
import dev.memocode.memo_server.usecase.CommentUseCase;
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
public class CommentService implements CommentUseCase {

    private final InternalMemoService internalMemoService;
    private final InternalCommentService internalCommentService;
    private final AuthorService authorService;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public UUID createComments(CommentCreateDTO dto) {
        Memo memo = internalMemoService.findByMemoIdElseThrow(dto.getMemoId());
        Author author = authorService.findByIdElseThrow(dto.getAuthorId());

        internalCommentService.validPost(memo);

        Comment comment = Comment.builder()
                .content(dto.getContent())
                .author(author)
                .memo(memo)
                .build();

        Comment saveComment = commentRepository.save(comment);

        return saveComment.getId();
    }

    @Override
    @Transactional
    public void updateComments(CommentUpdateDTO dto) {
        Memo memo = internalMemoService.findByMemoIdElseThrow(dto.getMemoId());
        Comment comment = internalCommentService.findByCommentId(dto.getCommentId());

        internalCommentService.validPost(memo);
        internalCommentService.validOwner(comment.getAuthor().getId(), dto.getAuthorId());

        comment.update(dto.getContent());
    }

}
