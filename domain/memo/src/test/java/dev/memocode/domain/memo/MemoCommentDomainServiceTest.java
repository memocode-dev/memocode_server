package dev.memocode.domain.memo;

import dev.memocode.domain.core.BusinessRuleViolationException;
import dev.memocode.domain.core.ForbiddenException;
import dev.memocode.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static dev.memocode.domain.memo.MemoDomainErrorCode.*;
import static dev.memocode.domain.support.TestConstructorFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
@ContextConfiguration(classes = {MemoCommentDomainService.class}) // 테스트할 클래스를 지정해줍니다.
@ExtendWith(SpringExtension.class) // junit5와 스프링을 연동해주는 어노테이션
class MemoCommentDomainServiceTest {

    @Autowired private MemoCommentDomainService memoCommentDomainService;

    @Test
    @DisplayName("메모 댓글 생성")
    void createMemoCommentTest() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, true);
        CreateMemoCommentDomainDTO createMemoCommentDomainDTO = createCreateMemoCommentDomainDTO("comment");

        // when
        MemoComment memoComment = memoCommentDomainService.createMemoComment(memo, user, createMemoCommentDomainDTO);

        // then
        assertThat(memoComment).isNotNull();
        assertThat(memoComment.getContent()).isEqualTo("comment"); // 댓글 내용이 일치해야 함
    }

    @Test
    @DisplayName("메모 댓글 생성 - 삭제된 메모에 댓글 생성 시 예외 발생")
    void createMemoCommentWithDeletedMemo() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, true);
        memo.softDelete();
        CreateMemoCommentDomainDTO createMemoCommentDomainDTO = createCreateMemoCommentDomainDTO("comment");

        // when
        // then
        assertThatThrownBy(() -> memoCommentDomainService.createMemoComment(memo, user, createMemoCommentDomainDTO))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining(DELETED_MEMO.getDefaultMessage()); // 삭제된 메모에 댓글 생성 불가
    }

    @Test
    @DisplayName("메모 댓글 생성 - 비공개 메모에 댓글 생성 시 예외 발생")
    void createMemoCommentWithPrivateMemo() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, false);
        CreateMemoCommentDomainDTO createMemoCommentDomainDTO = createCreateMemoCommentDomainDTO("comment");

        // when
        // then
        assertThatThrownBy(() -> memoCommentDomainService.createMemoComment(memo, user, createMemoCommentDomainDTO))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining(NOT_VISIBILITY_MEMO.getDefaultMessage()); // 비공개 메모에 댓글 생성 불가
    }

    @Test
    @DisplayName("메모 댓글 수정")
    void updateMemoCommentTest() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, true);
        MemoComment memoComment = memo.addComment("comment", user);
        UpdateMemoCommentDomainDTO updateMemoCommentDomainDTO = createUpdateMemoCommentDomainDTO("updated comment");

        // when
        memoCommentDomainService.updateMemoComment(memo, memoComment, user, updateMemoCommentDomainDTO);

        // then
        assertThat(memoComment.getContent()).isEqualTo("updated comment"); // 댓글 내용이 수정되어야 함
    }

    @Test
    @DisplayName("메모 댓글 수정 - 삭제된 메모일 경우")
    void updateMemoCommentWithDeletedMemo() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, true);
        MemoComment memoComment = memo.addComment("comment", user);
        memo.softDelete();
        UpdateMemoCommentDomainDTO updateMemoCommentDomainDTO = createUpdateMemoCommentDomainDTO("updated comment");

        // when
        // then
        assertThatThrownBy(() -> memoCommentDomainService.updateMemoComment(memo, memoComment, user, updateMemoCommentDomainDTO))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining(DELETED_MEMO.getDefaultMessage()); // 삭제된 메모이므로 수정 불가
    }

    @Test
    @DisplayName("메모 댓글 수정 - 비공개 메모일 경우")
    void updateMemoCommentWithPrivateMemo() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, false);
        MemoComment memoComment = memo.addComment("comment", user);
        UpdateMemoCommentDomainDTO updateMemoCommentDomainDTO = createUpdateMemoCommentDomainDTO("updated comment");

        // when
        // then
        assertThatThrownBy(() -> memoCommentDomainService.updateMemoComment(memo, memoComment, user, updateMemoCommentDomainDTO))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining(NOT_VISIBILITY_MEMO.getDefaultMessage()); // 비공개 메모이므로 수정 불가
    }

    @Test
    @DisplayName("메모 댓글 수정 - 삭제된 댓글일 경우")
    void updateDeletedMemoComment() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, true);
        MemoComment memoComment = memo.addComment("comment", user);
        memoComment.softDelete();
        UpdateMemoCommentDomainDTO updateMemoCommentDomainDTO = createUpdateMemoCommentDomainDTO("updated comment");

        // when
        // then
        assertThatThrownBy(() -> memoCommentDomainService.updateMemoComment(memo, memoComment, user, updateMemoCommentDomainDTO))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining(DELETED_MEMO_COMMENT.getDefaultMessage()); // 이미 삭제된 댓글이므로 수정 불가
    }

    @Test
    @DisplayName("메모 댓글 수정 - 댓글 작성자가 아닐 경우")
    void updateMemoCommentWithNotOwner() {
        // given
        User user = createUser(true);
        User notOwner = createUser(false);
        Memo memo = createMemo(user, "title", "content", "summary", false, true);
        MemoComment memoComment = memo.addComment("comment", user);
        UpdateMemoCommentDomainDTO updateMemoCommentDomainDTO = createUpdateMemoCommentDomainDTO("updated comment");

        // when
        // then
        assertThatThrownBy(() -> memoCommentDomainService.updateMemoComment(memo, memoComment, notOwner, updateMemoCommentDomainDTO))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining(NOT_MEMO_COMMENT_OWNER.getDefaultMessage()); // 댓글 작성자가 아니므로 수정 불가
    }

    @Test
    @DisplayName("메모 댓글 삭제")
    void deleteMemoCommentTest() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, true);
        MemoComment memoComment = memo.addComment("comment", user);

        // when
        memoCommentDomainService.deleteMemoComment(memo, memoComment, user);

        // then
        assertThat(memoComment.getDeleted()).isTrue(); // 삭제된 댓글이어야 함
    }

    @Test
    @DisplayName("메모 댓글 삭제 - 삭제된 메모일 경우")
    void deleteMemoCommentWithDeletedMemo() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, true);
        MemoComment memoComment = memo.addComment("comment", user);
        memo.softDelete();

        // when
        // then
        assertThatThrownBy(() -> memoCommentDomainService.deleteMemoComment(memo, memoComment, user))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining(DELETED_MEMO.getDefaultMessage()); // 삭제된 메모이므로 삭제 불가
    }

    @Test
    @DisplayName("메모 댓글 삭제 - 비공개 메모일 경우")
    void deleteMemoCommentWithPrivateMemo() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, false);
        MemoComment memoComment = memo.addComment("comment", user);

        // when
        // then
        assertThatThrownBy(() -> memoCommentDomainService.deleteMemoComment(memo, memoComment, user))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining(NOT_VISIBILITY_MEMO.getDefaultMessage()); // 비공개 메모이므로 삭제 불가
    }

    @Test
    @DisplayName("메모 댓글 삭제 - 삭제된 댓글일 경우")
    void deleteDeletedMemoComment() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, true);
        MemoComment memoComment = memo.addComment("comment", user);
        memoComment.softDelete();

        // when
        // then
        assertThatThrownBy(() -> memoCommentDomainService.deleteMemoComment(memo, memoComment, user))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining(DELETED_MEMO_COMMENT.getDefaultMessage()); // 이미 삭제된 댓글이므로 삭제 불가
    }

    @Test
    @DisplayName("메모 댓글 삭제 - 댓글 작성자가 아닐 경우")
    void deleteMemoCommentWithNotOwner() {
        // given
        User user = createUser(true);
        User notOwner = createUser(false);
        Memo memo = createMemo(user, "title", "content", "summary", false, true);
        MemoComment memoComment = memo.addComment("comment", user);

        // when
        // then
        assertThatThrownBy(() -> memoCommentDomainService.deleteMemoComment(memo, memoComment, notOwner))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining(NOT_MEMO_COMMENT_OWNER.getDefaultMessage()); // 댓글 작성자가 아니므로 삭제 불가
    }

    @Test
    @DisplayName("메모 댓글 전체 조회")
    void findAllMemoCommentsTest() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, true);
        MemoComment comment1 = memo.addComment("comment1", user);
        MemoComment comment2 = memo.addComment("comment2", user);
        MemoComment comment3 = memo.addComment("comment3", user);

        // when
        List<MemoComment> allByParentMemoCommentIsNull = memoCommentDomainService.findAllByParentMemoCommentIsNull(memo);

        // then
        assertThat(allByParentMemoCommentIsNull)
                .containsExactlyInAnyOrder(comment1, comment2, comment3); // 모든 댓글이 조회되어야 함
    }

    @Test
    @DisplayName("메모 댓글 전체 조회 - 삭제된 댓글 제외")
    void findAllMemoCommentsWithoutDeletedMemoComment() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, true);
        MemoComment comment1 = memo.addComment("comment1", user);
        MemoComment comment2 = memo.addComment("comment2", user);
        MemoComment comment3 = memo.addComment("comment3", user);
        comment2.softDelete();

        // when
        List<MemoComment> allByParentMemoCommentIsNull = memoCommentDomainService.findAllByParentMemoCommentIsNull(memo);

        // then
        assertThat(allByParentMemoCommentIsNull.stream().filter(memoComment -> !memoComment.getDeleted()).toList())
                .containsExactlyInAnyOrder(comment1, comment3); // 삭제되지 않은 댓글만 조회되어야 함
    }

    @Test
    @DisplayName("메모 댓글 전체 조회 - 삭제된 메모일 경우")
    void findAllMemoCommentsWithDeletedMemo() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, true);
        memo.softDelete();

        // when
        // then
        assertThatThrownBy(() -> memoCommentDomainService.findAllByParentMemoCommentIsNull(memo))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining(DELETED_MEMO.getDefaultMessage()); // 메모가 삭제되었으므로 조회 불가
    }

    @Test
    @DisplayName("메모 댓글 전체 조회 - 비공개 메모일 경우")
    void findAllMemoCommentsWithPrivateMemo() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, false);

        // when
        // then
        assertThatThrownBy(() -> memoCommentDomainService.findAllByParentMemoCommentIsNull(memo))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining(NOT_VISIBILITY_MEMO.getDefaultMessage()); // 메모가 비공개이므로 조회 불가
    }

    @Test
    @DisplayName("대댓글 생성")
    void createChildMemoCommentTest() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, true);
        MemoComment parentMemoComment = memo.addComment("parent comment", user);
        CreateMemoCommentDomainDTO createMemoCommentDomainDTO = createCreateMemoCommentDomainDTO("child comment");

        // when
        MemoComment childMemoComment = memoCommentDomainService.createChildMemoComment(memo, parentMemoComment, user, createMemoCommentDomainDTO);

        // then
        assertThat(childMemoComment).isNotNull(); // 생성된 대댓글이 존재해야 함
        assertThat(childMemoComment.getContent()).isEqualTo("child comment"); // 대댓글의 내용이 일치해야 함
    }

    @Test
    @DisplayName("대댓글 생성 - 삭제된 메모일 경우")
    void createChildMemoCommentWithDeletedMemo() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, true);
        MemoComment parentMemoComment = memo.addComment("parent comment", user);
        memo.softDelete();
        CreateMemoCommentDomainDTO createMemoCommentDomainDTO = createCreateMemoCommentDomainDTO("child comment");

        // when
        // then
        assertThatThrownBy(() -> memoCommentDomainService.createChildMemoComment(memo, parentMemoComment, user, createMemoCommentDomainDTO))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining(DELETED_MEMO.getDefaultMessage()); // 메모가 삭제되었으므로 대댓글 생성 불가
    }

    @Test
    @DisplayName("대댓글 생성 - 비공개 메모일 경우")
    void createChildMemoCommentWithPrivateMemo() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, false);
        MemoComment parentMemoComment = memo.addComment("parent comment", user);
        CreateMemoCommentDomainDTO createMemoCommentDomainDTO = createCreateMemoCommentDomainDTO("child comment");

        // when
        // then
        assertThatThrownBy(() -> memoCommentDomainService.createChildMemoComment(memo, parentMemoComment, user, createMemoCommentDomainDTO))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining(NOT_VISIBILITY_MEMO.getDefaultMessage()); // 메모가 비공개이므로 대댓글 생성 불가
    }

    @Test
    @DisplayName("대댓글 생성 - 부모댓글이 삭제되었을 경우")
    void createChildMemoCommentWithDeletedParentMemoComment() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, true);
        MemoComment parentMemoComment = memo.addComment("parent comment", user);
        parentMemoComment.softDelete();
        CreateMemoCommentDomainDTO createMemoCommentDomainDTO = createCreateMemoCommentDomainDTO("child comment");

        // when
        // then
        assertThatThrownBy(() -> memoCommentDomainService.createChildMemoComment(memo, parentMemoComment, user, createMemoCommentDomainDTO))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining(DELETED_MEMO_COMMENT.getDefaultMessage()); // 부모댓글이 삭제되었으므로 대댓글 생성 불가
    }
}