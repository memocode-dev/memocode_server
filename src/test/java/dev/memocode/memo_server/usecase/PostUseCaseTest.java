package dev.memocode.memo_server.usecase;

import dev.memocode.memo_server.base.BaseTest;
import dev.memocode.memo_server.domain.base.exception.GlobalException;
import dev.memocode.memo_server.domain.memo.dto.request.MemoCreateDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoUpdateDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemoDetailDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemosDTO;
import dev.memocode.memo_server.domain.memo.dto.response.PostAuthorDTO;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PostUseCaseTest extends BaseTest {

    @Autowired
    private MemoUseCase memoUseCase;

    @Autowired
    private PostUseCase postUseCase;

    @Test
    @DisplayName("메모 생성 성공")
    void createMemo_success(){
        MemoCreateDTO dto1 = MemoCreateDTO.builder()
                .authorId(author.getId())
                .title("테스트 제목입니다.")
                .content("테스트 내용입니다.")
                .summary("요약 내용입니다.")
                .build();

        memoUseCase.createMemo(dto1);

        MemoCreateDTO dto2 = MemoCreateDTO.builder()
                .authorId(author.getId())
                .title("테스트 제목입니다.")
                .content("테스트 내용입니다.")
                .summary("요약 내용입니다.")
                .build();

        memoUseCase.createMemo(dto2);
        MemosDTO memos = memoUseCase.findMemos(author.getId());
        assertThat(memos.getData().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("메모 제목이 null 값이라 ConstraintViolationException 예외 발생")
    void createMemoNotTitle_fail(){
        MemoCreateDTO dto = MemoCreateDTO.builder()
                .authorId(author.getId())
                .content("테스트 내용입니다.")
                .summary("요약 내용입니다.")
                .build();

        assertThatThrownBy(() -> memoUseCase.createMemo(dto))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("제목이 비어있습니다.");
    }

    @Test
    @DisplayName("메모 수정 성공")
    void modifyMemo_success(){
        MemoCreateDTO dto = MemoCreateDTO.builder()
                .authorId(author.getId())
                .title("테스트 제목입니다.")
                .content("테스트 내용입니다.")
                .summary("요약 내용입니다.")
                .build();

        UUID memoId = memoUseCase.createMemo(dto);

        MemoUpdateDTO updateDTO = MemoUpdateDTO.builder()
                .memoId(memoId)
                .authorId(author.getId())
                .title("테스트 제목을 수정하였습니다.")
                .content("테스트 내용을 수정하였습니다.")
                .summary("테스트 요약을 수정하였습니다.")
                .visibility(false)
                .security(false)
                .bookmarked(false)
                .build();

        memoUseCase.updateMemo(updateDTO);
        MemoDetailDTO memo = memoUseCase.findMemo(memoId, author.getId());
        String modifiedTitle = memo.getTitle();
        String modifiedContent = memo.getContent();
        String modifiedSummary = memo.getSummary();

        assertEquals("테스트 제목을 수정하였습니다.", modifiedTitle);
        assertEquals("테스트 내용을 수정하였습니다.", modifiedContent);
        assertEquals("테스트 요약을 수정하였습니다.", modifiedSummary);
    }

    @Test
    @DisplayName("메모 수정은 작성자 외 다른 사람이 수정할 수 없다.(메모 접근 불가능)")
    void modifyMemo_fail(){
        MemoCreateDTO dto = MemoCreateDTO.builder()
                .authorId(author.getId())
                .title("테스트 제목입니다.")
                .content("테스트 내용입니다.")
                .summary("요약 내용입니다.")
                .build();

        UUID memoId = memoUseCase.createMemo(dto);

        MemoUpdateDTO updateDTO = MemoUpdateDTO.builder()
                .memoId(memoId)
                .authorId(UUID.randomUUID()) // 다른 사용자를 random UUID 표시
                .title("테스트 제목을 수정하였습니다.")
                .content("테스트 내용을 수정하였습니다.")
                .summary("테스트 요약을 수정하였습니다.")
                .visibility(false)
                .security(false)
                .bookmarked(false)
                .build();

        assertThatThrownBy(() -> memoUseCase.updateMemo(updateDTO))
                .isInstanceOf(GlobalException.class)
                .hasMessageContaining("메모에 접근할 권한이 없습니다.");
    }

    @Test
    @DisplayName("해당 사용자의 블로그 게시물 조회")
    void findAuthorAllPost_success() {
        MemoCreateDTO dto = MemoCreateDTO.builder()
                .authorId(author.getId())
                .title("테스트 제목입니다.")
                .content("테스트 내용입니다.")
                .summary("요약 내용입니다.")
                .build();

        UUID memoId = memoUseCase.createMemo(dto);

        MemoUpdateDTO updateDTO = MemoUpdateDTO.builder()
                .memoId(memoId)
                .authorId(author.getId())
                .visibility(true)
                .build();

        memoUseCase.updateMemo(updateDTO);

        Page<PostAuthorDTO> authorAllPost = postUseCase.findAllPostByAuthorId(author.getId(), 0, 10);

        assertThat(authorAllPost.getTotalElements()).isEqualTo(1L);
    }

    @Test
    @DisplayName("해당 사용자의 블로그에 대한 게시글 조회 실패 (visibility 체크 된것이 하나)")
    void findAuthorAllPost_Fail() {
        MemoCreateDTO dto1 = MemoCreateDTO.builder()
                .authorId(author.getId())
                .title("테스트 제목입니다.")
                .content("테스트 내용입니다.")
                .summary("요약 내용입니다.")
                .build();

        UUID memoId1 = memoUseCase.createMemo(dto1);

        MemoCreateDTO dto2 = MemoCreateDTO.builder()
                .authorId(author.getId())
                .title("테스트 제목입니다.")
                .content("테스트 내용입니다.")
                .summary("요약 내용입니다.")
                .build();

        memoUseCase.createMemo(dto2);

        MemoUpdateDTO updateDTO = MemoUpdateDTO.builder()
                .memoId(memoId1)
                .authorId(author.getId())
                .visibility(true)
                .build();

        memoUseCase.updateMemo(updateDTO);

        Page<PostAuthorDTO> authorAllPost = postUseCase.findAllPostByAuthorId(author.getId(), 0, 10);

        assertThat(authorAllPost.getTotalElements()).isNotEqualTo(2L);
    }
}