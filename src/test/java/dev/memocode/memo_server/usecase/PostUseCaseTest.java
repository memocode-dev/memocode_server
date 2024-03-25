package dev.memocode.memo_server.usecase;

import dev.memocode.memo_server.domain.author.entity.Author;
import dev.memocode.memo_server.domain.author.repository.AuthorRepository;
import dev.memocode.memo_server.domain.memo.dto.request.MemoCreateDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoUpdateDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemosDTO;
import dev.memocode.memo_server.domain.memo.dto.response.PostAuthorDTO;
import jakarta.validation.ConstraintViolationException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PostUseCaseTest {

    @Autowired
    private MemoUseCase memoUseCase;

    @Autowired
    private PostUseCase postUseCase;

    @Autowired
    private AuthorRepository authorRepository;

    private Author savedAuthor;

    @BeforeEach
    void setUp() {
        savedAuthor = createTestAuthor();
    }

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

    @Test
    @DisplayName("메모 생성 성공")
    void createMemo_success(){
        MemoCreateDTO dto1 = MemoCreateDTO.builder()
                .authorId(savedAuthor.getId())
                .title("테스트 제목입니다.")
                .content("테스트 내용입니다.")
                .summary("요약 내용입니다.")
                .build();

        memoUseCase.createMemo(dto1);

        MemoCreateDTO dto2 = MemoCreateDTO.builder()
                .authorId(savedAuthor.getId())
                .title("테스트 제목입니다.")
                .content("테스트 내용입니다.")
                .summary("요약 내용입니다.")
                .build();

        memoUseCase.createMemo(dto2);
        MemosDTO memos = memoUseCase.findMemos(savedAuthor.getId());
        assertThat(memos.getData().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("메모 제목이 null 값이라 ConstraintViolationException 예외 발생")
    void createMemoNotTitle_fail(){
        MemoCreateDTO dto = MemoCreateDTO.builder()
                .authorId(savedAuthor.getId())
                .content("테스트 내용입니다.")
                .summary("요약 내용입니다.")
                .build();

        assertThrows(ConstraintViolationException.class, () -> {
            memoUseCase.createMemo(dto);
        });
    }

    @Test
    @DisplayName("해당 사용자의 블로그 게시물 조회")
    void findAuthorAllPost_success() {
        MemoCreateDTO dto = MemoCreateDTO.builder()
                .authorId(savedAuthor.getId())
                .title("테스트 제목입니다.")
                .content("테스트 내용입니다.")
                .summary("요약 내용입니다.")
                .build();

        UUID memoId = memoUseCase.createMemo(dto);

        MemoUpdateDTO updateDTO = MemoUpdateDTO.builder()
                .memoId(memoId)
                .authorId(savedAuthor.getId())
                .visibility(true)
                .build();

        memoUseCase.updateMemo(updateDTO);

        Page<PostAuthorDTO> authorAllPost = postUseCase.findAllPostByAuthorId(savedAuthor.getId(), 0, 10);

        assertThat(authorAllPost.getTotalElements()).isEqualTo(1L);
    }

    @Test
    @DisplayName("해당 사용자의 블로그에 대한 게시글 조회 실패 (visibility 체크 된것이 하나)")
    void findAuthorAllPost_Fail() {
        MemoCreateDTO dto1 = MemoCreateDTO.builder()
                .authorId(savedAuthor.getId())
                .title("테스트 제목입니다.")
                .content("테스트 내용입니다.")
                .summary("요약 내용입니다.")
                .build();

        UUID memoId1 = memoUseCase.createMemo(dto1);

        MemoCreateDTO dto2 = MemoCreateDTO.builder()
                .authorId(savedAuthor.getId())
                .title("테스트 제목입니다.")
                .content("테스트 내용입니다.")
                .summary("요약 내용입니다.")
                .build();

        memoUseCase.createMemo(dto2);

        MemoUpdateDTO updateDTO = MemoUpdateDTO.builder()
                .memoId(memoId1)
                .authorId(savedAuthor.getId())
                .visibility(true)
                .build();

        memoUseCase.updateMemo(updateDTO);

        Page<PostAuthorDTO> authorAllPost = postUseCase.findAllPostByAuthorId(savedAuthor.getId(), 0, 10);

        assertThat(authorAllPost.getTotalElements()).isNotEqualTo(2L);
    }
}