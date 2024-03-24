package dev.memocode.memo_server.usecase;

import dev.memocode.memo_server.domain.author.entity.Author;
import dev.memocode.memo_server.domain.author.repository.AuthorRepository;
import dev.memocode.memo_server.domain.memo.dto.request.MemoCreateDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoUpdateDTO;
import dev.memocode.memo_server.domain.memocomment.dto.request.ChildCommentCreateDTO;
import dev.memocode.memo_server.domain.memocomment.dto.request.CommentCreateDTO;
import dev.memocode.memo_server.domain.memocomment.dto.request.CommentDeleteDto;
import dev.memocode.memo_server.domain.memocomment.dto.response.CommentsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CommentUseCaseTest {

    @Autowired
    private CommentUseCase commentUseCase;

    @Autowired
    private MemoUseCase memoUseCase;

    @Autowired
    private AuthorRepository authorRepository;

    private Author savedAuthor;
    private UUID memoId;

    @BeforeEach
    void setUp() {
        savedAuthor = createTestAuthor();
        memoId = createTestMemo(savedAuthor.getId());
    }

    /**
     * 유저 생성
     */
    private Author createTestAuthor() {
        Author author = Author.builder()
                .id(UUID.randomUUID())
                .username("테스트이름")
                .nickname("테스트닉네임")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .deleted(false)
                .deletedAt(null)
                .build();
        return authorRepository.save(author);
    }

    /**
     * 게시글 생성 (visibility = true)
     */
    private UUID createTestMemo(UUID authorId) {
        MemoCreateDTO dto = MemoCreateDTO.builder()
                .authorId(authorId)
                .title("테스트 제목입니다.")
                .content("테스트 내용입니다.")
                .build();

        UUID memoId = memoUseCase.createMemo(dto);

        MemoUpdateDTO updateDTO = MemoUpdateDTO.builder()
                .memoId(memoId)
                .authorId(authorId)
                .visibility(true)
                .build();

        memoUseCase.updateMemo(updateDTO);

        return memoId;
    }

    @Test
    @DisplayName("부모 댓글 삭제 할 경우 자식 댓글 연쇄 삭제")
    void deleteComments_success() {
        // 1번 부모 댓글 생성
        CommentCreateDTO comment1 = CommentCreateDTO.builder()
                .memoId(memoId)
                .content("1번 게시글의 테스트 댓글입니다.")
                .authorId(savedAuthor.getId())
                .build();

        UUID comments1 = commentUseCase.createComment(comment1);

        // 2번 부모 댓글 생성
        CommentCreateDTO comment2 = CommentCreateDTO.builder()
                .memoId(memoId)
                .content("1번 게시글의 테스트 댓글입니다.")
                .authorId(savedAuthor.getId())
                .build();

        commentUseCase.createComment(comment2);

        // 자식 댓글 생성
        ChildCommentCreateDTO childDto = ChildCommentCreateDTO.builder()
                .memoId(memoId)
                .content("1번 게시글의 1번 댓글의 대댓글입니다.")
                .authorId(savedAuthor.getId())
                .commentId(comments1)
                .build();

        commentUseCase.createChildComment(childDto);

        // 1번 부모 댓글 삭제 -> 댓글 3개에서 1개가 되야한다.
        CommentDeleteDto deleteDto = CommentDeleteDto.builder()
                .memoId(memoId)
                .commentId(comments1)
                .authorId(savedAuthor.getId())
                .build();

        commentUseCase.deleteComments(deleteDto);

        Page<CommentsDTO> allComments = commentUseCase.findAllComments(memoId, 0, 10);
        assertThat(allComments.getTotalElements()).isEqualTo(1L);
    }

    @Test
    @DisplayName("부모 댓글 삭제 할 경우 자식 댓글 연쇄 삭제 실패")
    void deleteComments_fail() {
        // 1번 부모 댓글 생성
        CommentCreateDTO comment1 = CommentCreateDTO.builder()
                .memoId(memoId)
                .content("1번 게시글의 테스트 댓글입니다.")
                .authorId(savedAuthor.getId())
                .build();

        UUID comments1 = commentUseCase.createComment(comment1);

        // 2번 부모 댓글 생성
        CommentCreateDTO comment2 = CommentCreateDTO.builder()
                .memoId(memoId)
                .content("1번 게시글의 테스트 댓글입니다.")
                .authorId(savedAuthor.getId())
                .build();

        commentUseCase.createComment(comment2);

        // 자식 댓글 생성
        ChildCommentCreateDTO childDto = ChildCommentCreateDTO.builder()
                .memoId(memoId)
                .content("1번 게시글의 1번 댓글의 대댓글입니다.")
                .authorId(savedAuthor.getId())
                .commentId(comments1)
                .build();

        commentUseCase.createChildComment(childDto);

        // 1번 부모 댓글 삭제 -> 댓글 3개에서 1개가 되야한다.
        CommentDeleteDto deleteDto = CommentDeleteDto.builder()
                .memoId(memoId)
                .commentId(comments1)
                .authorId(savedAuthor.getId())
                .build();

        commentUseCase.deleteComments(deleteDto);

        Page<CommentsDTO> allComments = commentUseCase.findAllComments(memoId, 0, 10);
        // 만약 연쇄 삭제가 되지 않는다면 2가 나와야 한다.
        assertThat(allComments.getTotalElements()).isNotEqualTo(2L);
    }
}