package dev.memocode.domain.memo;

import dev.memocode.domain.core.BusinessRuleViolationException;
import dev.memocode.domain.core.ForbiddenException;
import dev.memocode.domain.core.ValidationException;
import dev.memocode.domain.user.ImmutableUser;
import dev.memocode.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.List;

import static dev.memocode.domain.memo.MemoDomainErrorCode.*;
import static dev.memocode.domain.support.TestConstructorFactory.*;
import static dev.memocode.domain.user.UserDomainErrorCode.USER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ContextConfiguration(classes = {MemoDomainService.class}) // 테스트할 클래스를 지정해줍니다.
@ExtendWith(SpringExtension.class) // junit5와 스프링을 연동해주는 어노테이션
class MemoDomainServiceTest {

    @Autowired private MemoDomainService memoDomainService;

    @Test
    @DisplayName("메모 생성")
    void createMemoTest() {
        //given
        User user = createUser(true);

        MemoCreateDomainDTO memoCreateDomainDTO = createMemoCreateDomainDTO(user, "title", "content", "summary", false);
        //when
        Memo memo = memoDomainService.createMemo(memoCreateDomainDTO);
        //then
        assertThat(memo.getId()).isNotNull(); // ID는 null이 아니어야 합니다.
        assertThat(memo.getTitle()).isEqualTo("title"); // 제목이 입력한 제목과 일치해야 합니다.
        assertThat(memo.getContent()).isEqualTo("content"); // 내용이 입력한 내용과 일치해야 합니다.
        assertThat(memo.getSummary()).isEqualTo("summary"); // 요약이 입력한 요약과 일치해야 합니다.
        assertThat(memo.getVisibility()).isFalse(); // 가시성은 false여야 합니다.
        assertThat(memo.getSecurity()).isFalse(); // 보안은 false여야 합니다.
        assertThat(memo.getBookmarked()).isFalse(); // 북마크는 false여야 합니다.
        assertThat(memo.getUser()).isEqualTo(user); // 사용자가 입력한 사용자와 일치해야 합니다.
        assertThat(memo.getDeleted()).isFalse(); // 삭제 여부는 false여야 합니다.
    }

    @Test
    @DisplayName("메모 생성 - 활성화되지 않은 유저가 메모를 생성하려고 할 때 예외 발생")
    void createMemoTestWhenUserIsNotEnabled() {
        //given
        User user = createUser(false);

        MemoCreateDomainDTO memoCreateDomainDTO = createMemoCreateDomainDTO(user, "title", "content", "summary", false);
        //when
        //then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> memoDomainService.createMemo(memoCreateDomainDTO))
                .withMessage(USER_NOT_FOUND.getDefaultMessage()); // 활성화된 유저가 아닌 경우 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("메모 수정")
    void updateMemoTest() {
        //given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, true);

        MemoUpdateDomainDTO memoUpdateDomainDTO = createMemoUpdateDomainDTO("updated title", "updated content", "updated summary", null, true, true);
        //when
        memoDomainService.updateMemo(memo, user, memoUpdateDomainDTO);
        //then
        assertThat(memo.getTitle()).isEqualTo("updated title"); // 제목이 수정된 제목과 일치해야 합니다.
        assertThat(memo.getContent()).isEqualTo("updated content"); // 내용이 수정된 내용과 일치해야 합니다.
        assertThat(memo.getSummary()).isEqualTo("updated summary"); // 요약이 수정된 요약과 일치해야 합니다.
        assertThat(memo.getVisibility()).isTrue(); // 가시성은 true여야 합니다.
        assertThat(memo.getSecurity()).isFalse(); // 보안은 true여야 합니다.
        assertThat(memo.getBookmarked()).isTrue(); // 북마크는 true여야 합니다.
    }

    @Test
    @DisplayName("메모 수정 - 활성화되지 않은 유저가 메모를 수정하려고 할 때 예외 발생")
    void updateMemoTestWhenUserIsNotEnabled() {
        //given
        User user = createUser(false);
        Memo memo = createMemo(user, "title", "content", "summary", false, true);

        MemoUpdateDomainDTO memoUpdateDomainDTO = createMemoUpdateDomainDTO("updated title", "updated content", "updated summary", null, true, true);
        //when
        //then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> memoDomainService.updateMemo(memo, user, memoUpdateDomainDTO))
                .withMessage(USER_NOT_FOUND.getDefaultMessage()); // 활성화된 유저가 아닌 경우 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("메모 수정 - 삭제된 메모를 수정하려고 할 때 예외 발생")
    void updateMemoTestWhenMemoIsDeleted() {
        //given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, true);
        memo.softDelete();

        MemoUpdateDomainDTO memoUpdateDomainDTO = createMemoUpdateDomainDTO("updated title", "updated content", "updated summary", null, true, true);
        //when
        //then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> memoDomainService.updateMemo(memo, user, memoUpdateDomainDTO))
                .withMessage("삭제된 메모입니다."); // 삭제된 메모인 경우 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("메모 수정 - 메모의 소유자가 아닌 유저가 메모를 수정하려고 할 때 예외 발생")
    void updateMemoTestWhenUserIsNotMemoOwner() {
        //given
        User user = createUser(true);
        User notOwner = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, true);

        MemoUpdateDomainDTO memoUpdateDomainDTO = createMemoUpdateDomainDTO("updated title", "updated content", "updated summary", null, true, true);
        //when
        //then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> memoDomainService.updateMemo(memo, notOwner, memoUpdateDomainDTO))
                .withMessage("메모의 소유자가 아닙니다."); // 메모의 소유자가 아닌 경우 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("메모 수정 - 동시에 보안과 공개성을 수정하려고 할 때 예외 발생")
    void updateMemoTestWhenSecurityAndVisibilityAreUpdatedTogether() {
        //given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, false);

        MemoUpdateDomainDTO memoUpdateDomainDTO = createMemoUpdateDomainDTO("updated title", "updated content", "updated summary", true, true, true);
        //when
        //then
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> memoDomainService.updateMemo(memo, user, memoUpdateDomainDTO))
                .withMessage(CANNOT_UPDATE_SECURITY_AND_VISIBILITY_TOGETHER.getDefaultMessage()); // 보안과 공개성을 동시에 수정하려고 할 때 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("메모 수정 - 보안이 활성화된 메모의 보안을 다시 수정하려고 할 때 예외 발생")
    void updateMemoTestWhenSecurityIsActivated() {
        //given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", true, true);

        MemoUpdateDomainDTO memoUpdateDomainDTO = createMemoUpdateDomainDTO("updated title", "updated content", "updated summary", false, null, true);
        //when
        //then
        assertThatExceptionOfType(BusinessRuleViolationException.class)
                .isThrownBy(() -> memoDomainService.updateMemo(memo, user, memoUpdateDomainDTO))
                .withMessage(PROTECT_MEMO_SECURITY_UNMODIFIED.getDefaultMessage()); // 보안이 활성화된 메모의 보안을 수정하려고 할 때 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("메모 수정 - 보안이 활성화된 메모의 공개성을 수정하려고 할 때 예외 발생")
    void updateMemoTestWhenSecurityIsActivatedAndVisibilityIsUpdated() {
        //given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", true, false);

        MemoUpdateDomainDTO memoUpdateDomainDTO = createMemoUpdateDomainDTO("updated title", "updated content", "updated summary", null, true, true);
        //when
        //then
        assertThatExceptionOfType(BusinessRuleViolationException.class)
                .isThrownBy(() -> memoDomainService.updateMemo(memo, user, memoUpdateDomainDTO))
                .withMessage(PROTECT_MEMO_VISIBILITY_UNMODIFIED.getDefaultMessage()); // 보안이 활성화된 메모의 공개성을 수정하려고 할 때 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("메모 수정 - 이미 공개된 메모의 보안을 수정하려고 할 때 예외 발생")
    void updateMemoTestWhenVisibilityIsActivated() {
        //given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, Instant.MIN);

        MemoUpdateDomainDTO memoUpdateDomainDTO = createMemoUpdateDomainDTO("updated title", "updated content", "updated summary", true, null, true);
        //when
        //then
        assertThatExceptionOfType(BusinessRuleViolationException.class)
                .isThrownBy(() -> memoDomainService.updateMemo(memo, user, memoUpdateDomainDTO))
                .withMessage(PROTECT_MODE_DISABLED_ONCE_PUBLIC.getDefaultMessage()); // 이미 공개된 메모의 보안을 수정하려고 할 때 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("메모 삭제")
    void softDeleteMemoTest() {
        //given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, true);
        //when
        memoDomainService.softDeleteMemo(memo, user);
        //then
        assertThat(memo.getDeleted()).isTrue(); // 삭제 여부는 true여야 합니다.
    }

    @Test
    @DisplayName("메모 삭제 - 삭제된 메모를 다시 삭제하려고 할 때 예외 발생")
    void softDeleteMemoTestWhenMemoIsDeleted() {
        //given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, true);
        memo.softDelete();
        //when
        //then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> memoDomainService.softDeleteMemo(memo, user))
                .withMessage(DELETED_MEMO.getDefaultMessage()); // 삭제된 메모를 다시 삭제하려고 할 때 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("메모 삭제 - 메모의 소유자가 아닌 유저가 메모를 삭제하려고 할 때 예외 발생")
    void softDeleteMemoTestWhenUserIsNotMemoOwner() {
        //given
        User user = createUser(true);
        User notOwner = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, true);
        //when
        //then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> memoDomainService.softDeleteMemo(memo, notOwner))
                .withMessage(NOT_MEMO_OWNER.getDefaultMessage()); // 메모의 소유자가 아닌 경우 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("본인 메모 조회")
    void findMyMemoTest() {
        //given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, true);
        //when
        Memo myMemo = memoDomainService.findMyMemo(memo, user);
        //then
        assertThat(myMemo).isEqualTo(memo); // 메모가 일치해야 합니다.
    }

    @Test
    @DisplayName("본인 메모 조회 - 삭제된 메모를 조회하려고 할 때 예외 발생")
    void findMyMemoTestWhenMemoIsDeleted() {
        //given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, true);
        memo.softDelete();
        //when
        //then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> memoDomainService.findMyMemo(memo, user))
                .withMessage(DELETED_MEMO.getDefaultMessage()); // 삭제된 메모를 조회하려고 할 때 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("본인 메모 조회 - 메모의 소유자가 아닌 유저가 메모를 조회하려고 할 때 예외 발생")
    void findMyMemoTestWhenUserIsNotMemoOwner() {
        //given
        User user = createUser(true);
        User notOwner = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, true);
        //when
        //then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> memoDomainService.findMyMemo(memo, notOwner))
                .withMessage(NOT_MEMO_OWNER.getDefaultMessage()); // 메모의 소유자가 아닌 경우 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("본인 메모 전체 조회")
    void findAllMyMemoTest() {
        //given
        User user = createUser(true);
        Memo memo1 = createMemo(user, "title1", "content1", "summary1", false, false);
        Memo memo2 = createMemo(user, "title2", "content2", "summary2", false, false);
        Memo memo3 = createMemo(user, "title3", "content3", "summary3", false, false);
        //when
        //then
        assertThat(memoDomainService.findAllMyMemo(List.of(memo1, memo2, memo3), user))
                .containsExactlyInAnyOrder(memo1, memo2, memo3); // 메모가 일치해야 합니다.
    }

    @Test
    @DisplayName("본인 메모 전체 조회 - 삭제된 메모 제외")
    void findAllMyMemoTestWhenMemoIsDeleted() {
        //given
        User user = createUser(true);
        Memo memo1 = createMemo(user, "title1", "content1", "summary1", false, false);
        Memo memo2 = createMemo(user, "title2", "content2", "summary2", false, false);
        Memo memo3 = createMemo(user, "title3", "content3", "summary3", false, false);
        memo2.softDelete();
        //when
        //then
        assertThat(memoDomainService.findAllMyMemo(List.of(memo1, memo2, memo3), user))
                .containsExactlyInAnyOrder(memo1, memo3); // 삭제된 메모는 조회되지 않아야 합니다.
    }

    @Test
    @DisplayName("본인 메모 검색")
    void searchMyMemoTest() {
        //given
        ImmutableUser immutableUser = createImmutableUser();
        User user = immutableUserToUser(immutableUser);// immutableUser를 User로 변환합니다.
        ImmutableMemo memo1 = createImmutableMemo(immutableUser, "title1", "content1", "summary1", false, false);
        ImmutableMemo memo2 = createImmutableMemo(immutableUser, "title2", "content2", "summary2", false, false);
        ImmutableMemo memo3 = createImmutableMemo(immutableUser, "title3", "content3", "summary3", false, false);
        //when
        //then
        assertThat(memoDomainService.searchMyMemo(List.of(memo1, memo2, memo3), user))
                .containsExactlyInAnyOrder(memo1, memo2, memo3); // 메모가 일치해야 합니다.
    }

    @Test
    @DisplayName("본인 메모 검색 - 삭제된 메모를 검색하려고 할 때 예외 발생")
    void searchMyMemoTestWhenMemoIsDeleted() {
        //given
        ImmutableUser immutableUser = createImmutableUser();
        User user = immutableUserToUser(immutableUser);// immutableUser를 User로 변환합니다.
        ImmutableMemo memo1 = createImmutableMemo(immutableUser, "title1", "content1", "summary1", false, false);
        ImmutableMemo memo2 = createImmutableMemo(immutableUser, "title2", "content2", "summary2", false, true);
        ImmutableMemo memo3 = createImmutableMemo(immutableUser, "title3", "content3", "summary3", false, false);
        //when
        //then
        assertThat(memoDomainService.searchMyMemo(List.of(memo1, memo2, memo3), user))
                .containsExactlyInAnyOrder(memo1, memo3); // 삭제된 메모는 조회되지 않아야 합니다.
    }

    @Test
    @DisplayName("본인 메모 검색 - 다른 사용자의 메모를 검색하려고 할 때 예외 발생")
    void searchMyMemoTestWhenUserIsNotMemoOwner() {
        //given
        ImmutableUser immutableUser = createImmutableUser();
        User user = immutableUserToUser(immutableUser);// immutableUser를 User로 변환합니다.
        ImmutableUser notOwner = createImmutableUser();
        ImmutableMemo memo1 = createImmutableMemo(immutableUser, "title1", "content1", "summary1", false, false);
        ImmutableMemo memo2 = createImmutableMemo(notOwner, "title2", "content2", "summary2", false, false);
        ImmutableMemo memo3 = createImmutableMemo(immutableUser, "title3", "content3", "summary3", false, false);
        //when
        //then
        assertThat(memoDomainService.searchMyMemo(List.of(memo1, memo2, memo3), user))
                .containsExactlyInAnyOrder(memo1, memo3); // 다른 사용자의 메모는 조회되지 않아야 합니다.
    }

    @Test
    @DisplayName("메모 조회")
    void findMemoTest() {
        //given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, true);
        //when
        Memo foundMemo = memoDomainService.findMemo(memo);
        //then
        assertThat(foundMemo).isEqualTo(memo); // 메모가 일치해야 합니다.
    }

    @Test
    @DisplayName("메모 조회 - 삭제된 메모를 조회하려고 할 때 예외 발생")
    void findMemoTestWhenMemoIsDeleted() {
        //given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", false, true);
        memo.softDelete();
        //when
        //then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> memoDomainService.findMemo(memo))
                .withMessage(DELETED_MEMO.getDefaultMessage()); // 삭제된 메모를 조회하려고 할 때 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("메모 조회 - 공개되지 않은 메모를 조회하려고 할 때 예외 발생")
    void findMemoTestWhenMemoIsNotVisible() {
        //given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", true, false);
        //when
        //then
        assertThatExceptionOfType(BusinessRuleViolationException.class)
                .isThrownBy(() -> memoDomainService.findMemo(memo))
                .withMessage(NOT_VISIBILITY_MEMO.getDefaultMessage()); // 공개되지 않은 메모를 조회하려고 할 때 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("메모 검색")
    void searchMemoTest() {
        //given
        ImmutableUser immutableUser = createImmutableUser();
        ImmutableMemo memo1 = createImmutableMemo(immutableUser, "title1", "content1", "summary1", true, false);
        ImmutableMemo memo2 = createImmutableMemo(immutableUser, "title2", "content2", "summary2", true, false);
        ImmutableMemo memo3 = createImmutableMemo(immutableUser, "title3", "content3", "summary3", true, false);
        //when
        //then
        assertThat(memoDomainService.searchMemo(List.of(memo1, memo2, memo3)))
                .containsExactlyInAnyOrder(memo1, memo2, memo3); // 메모가 일치해야 합니다.
    }

    @Test
    @DisplayName("메모 검색 - 삭제된 메모를 검색하려고 할 때 예외 발생")
    void searchMemoTestWhenMemoIsDeleted() {
        //given
        ImmutableUser immutableUser = createImmutableUser();
        ImmutableMemo memo1 = createImmutableMemo(immutableUser, "title1", "content1", "summary1", true, false);
        ImmutableMemo memo2 = createImmutableMemo(immutableUser, "title2", "content2", "summary2", true, true);
        ImmutableMemo memo3 = createImmutableMemo(immutableUser, "title3", "content3", "summary3", true, false);
        //when
        //then
        assertThat(memoDomainService.searchMemo(List.of(memo1, memo2, memo3)))
                .containsExactlyInAnyOrder(memo1, memo3); // 삭제된 메모는 조회되지 않아야 합니다.
    }

    @Test
    @DisplayName("메모 검색 - 공개되지 않은 메모를 검색하려고 할 때 예외 발생")
    void searchMemoTestWhenMemoIsNotVisible() {
        //given
        ImmutableUser immutableUser = createImmutableUser();
        ImmutableMemo memo1 = createImmutableMemo(immutableUser, "title1", "content1", "summary1", false, false);
        ImmutableMemo memo2 = createImmutableMemo(immutableUser, "title2", "content2", "summary2", true, false);
        ImmutableMemo memo3 = createImmutableMemo(immutableUser, "title3", "content3", "summary3", false, false);
        //when
        //then
        assertThat(memoDomainService.searchMemo(List.of(memo1, memo2, memo3)))
                .containsExactlyInAnyOrder(memo2); // 공개되지 않은 메모는 조회되지 않아야 합니다.
    }


}