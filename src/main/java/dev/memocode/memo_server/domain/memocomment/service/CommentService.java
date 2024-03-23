package dev.memocode.memo_server.domain.memocomment.service;

import dev.memocode.memo_server.domain.author.entity.Author;
import dev.memocode.memo_server.domain.author.service.AuthorService;
import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memo.service.InternalMemoService;
import dev.memocode.memo_server.domain.memocomment.dto.request.ChildCommentCreateDTO;
import dev.memocode.memo_server.domain.memocomment.dto.request.CommentCreateDTO;
import dev.memocode.memo_server.domain.memocomment.dto.request.CommentDeleteDto;
import dev.memocode.memo_server.domain.memocomment.dto.request.CommentUpdateDTO;
import dev.memocode.memo_server.domain.memocomment.dto.response.CommentsDTO;
import dev.memocode.memo_server.domain.memocomment.entity.Comment;
import dev.memocode.memo_server.domain.memocomment.mapper.CommentMapper;
import dev.memocode.memo_server.domain.memocomment.repository.CommentRepository;
import dev.memocode.memo_server.usecase.CommentUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService implements CommentUseCase {

    private final InternalMemoService internalMemoService;
    private final InternalCommentService internalCommentService;
    private final AuthorService authorService;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public UUID createComment(CommentCreateDTO dto) {
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

    @Override
    @Transactional
    public void deleteComments(CommentDeleteDto dto) {
        Memo memo = internalMemoService.findByMemoIdElseThrow(dto.getMemoId());
        Comment comment = internalCommentService.findByCommentId(dto.getCommentId());

        internalCommentService.validPost(memo);
        internalCommentService.validOwner(comment.getAuthor().getId(), dto.getAuthorId());

        comment.delete();
        // 자식 댓글또한 연쇄 삭제
        comment.getChildComments().forEach(Comment::delete);
    }

    @Override
    public Page<CommentsDTO> findAllComments(UUID memoId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        // 존재하는 게시글인지 찾기
        internalMemoService.findByMemoIdElseThrow(memoId);

        Page<Comment> comments = commentRepository.findAllByMemoId(memoId, pageRequest);

        return commentMapper.entity_to_commentsDto(comments);
    }

    @Override
    @Transactional
    public UUID createChildComment(ChildCommentCreateDTO dto) {
        Memo memo = internalMemoService.findByMemoIdElseThrow(dto.getMemoId());
        Author author = authorService.findByIdElseThrow(dto.getAuthorId());
        Comment comment = internalCommentService.findByCommentId(dto.getCommentId());

        internalCommentService.validPost(memo);

        Comment childComment = Comment.builder()
                .content(dto.getContent())
                .author(author)
                .memo(memo)
                .parentComment(comment)
                .build();

        Comment saveChildComment = commentRepository.save(childComment);

        return saveChildComment.getId();
    }

}
