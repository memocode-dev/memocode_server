package dev.memocode.domain.memo;

import dev.memocode.domain.core.ForbiddenException;
import dev.memocode.domain.core.NotFoundException;
import dev.memocode.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static dev.memocode.domain.memo.MemoDomainErrorCode.*;
import static dev.memocode.domain.support.TestConstructorFactory.createMemo;
import static dev.memocode.domain.support.TestConstructorFactory.createUser;
import static dev.memocode.domain.user.UserDomainErrorCode.USER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ContextConfiguration(classes = {MemoVersionDomainService.class}) // 테스트할 클래스를 지정해줍니다.
@ExtendWith(SpringExtension.class) // junit5와 스프링을 연동해주는 어노테이션
class MemoVersionDomainServiceTest {

    @Autowired private MemoVersionDomainService memoVersionDomainService;

    @Test
    @DisplayName("메모 버전 생성")
    void createMemoVersionTest() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", true, true);

        // when
        MemoVersion memoVersion = memoVersionDomainService.createMemoVersion(memo, user);

        // then
        assertThat(memoVersion).isNotNull(); // memoVersion이 null이 아닌지 확인합니다.
        assertThat(memoVersion.getMemo()).isEqualTo(memo); // memoVersion의 memo가 memo와 같은지 확인합니다.
        assertThat(memoVersion.getContent()).isEqualTo("content"); // memoVersion의 content가 "content"와 같은지 확인합니다.
        assertThat(memoVersion.getVersion()).isEqualTo(1); // memoVersion의 version이 1인지 확인합니다.
    }

    @Test
    @DisplayName("메모 버젼 생성 - 활성화되지 않은 유저가 메모 버젼을 생성하려고 할 때 예외 발생")
    void createMemoVersionWithDisabledUser() {
        // given
        User user = createUser(false);
        Memo memo = createMemo(user, "title", "content", "summary", true, true);

        // when
        // then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> memoVersionDomainService.createMemoVersion(memo, user))
                .withMessage(USER_NOT_FOUND.getDefaultMessage()); // 예외 메시지가 USER_NOT_FOUND와 같은지 확인합니다.
    }

    @Test
    @DisplayName("메모 버전 생성 - 삭제된 메모에 버전을 생성하려고 할 때 예외 발생")
    void createMemoVersionWithDeletedMemo() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", true, false);
        memo.softDelete();

        // when
        // then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> memoVersionDomainService.createMemoVersion(memo, user))
                .withMessage(DELETED_MEMO.getDefaultMessage()); // 예외 메시지가 DELETED_MEMO와 같은지 확인합니다.
    }

    @Test
    @DisplayName("메모 버전 생성 - 다른 유저가 메모 버전을 생성하려고 할 때 예외 발생")
    void createMemoVersionWithOtherUser() {
        // given
        User owner = createUser(true);
        User notOwner = createUser(true);
        Memo memo = createMemo(notOwner, "title", "content", "summary", true, true);

        // when
        // then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> memoVersionDomainService.createMemoVersion(memo, owner))
                .withMessage(NOT_MEMO_OWNER.getDefaultMessage()); // 예외 메시지가 NOT_MEMO_OWNER와 같은지 확인합니다.
    }

    @Test
    @DisplayName("메모 버전 삭제")
    void removeMemoVersionTest() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", true, true);
        MemoVersion memoVersion = memo.addVersion();

        // when
        memoVersionDomainService.removeMemoVersion(memo, user, memoVersion.getId());

        // then
        assertThat(memoVersion.getDeleted()).isTrue(); // memoVersion이 삭제되었는지 확인합니다.
    }

    @Test
    @DisplayName("메모 버전 삭제 - 활성화되지 않은 유저가 메모 버전을 삭제하려고 할 때 예외 발생")
    void removeMemoVersionWithDisabledUser() {
        // given
        User user = createUser(false);
        Memo memo = createMemo(user, "title", "content", "summary", true, true);
        MemoVersion memoVersion = memo.addVersion();

        // when
        // then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> memoVersionDomainService.removeMemoVersion(memo, user, memoVersion.getId()))
                .withMessage(USER_NOT_FOUND.getDefaultMessage()); // 예외 메시지가 USER_NOT_FOUND와 같은지 확인합니다.
    }

    @Test
    @DisplayName("메모 버전 삭제 - 삭제된 메모에 버전을 삭제하려고 할 때 예외 발생")
    void removeMemoVersionWithDeletedMemo() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", true, true);
        MemoVersion memoVersion = memo.addVersion();
        memo.softDelete();

        // when
        // then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> memoVersionDomainService.removeMemoVersion(memo, user, memoVersion.getId()))
                .withMessage(DELETED_MEMO.getDefaultMessage()); // 예외 메시지가 DELETED_MEMO와 같은지 확인합니다.
    }

    @Test
    @DisplayName("메모 버전 삭제 - 다른 유저가 메모 버전을 삭제하려고 할 때 예외 발생")
    void removeMemoVersionWithOtherUser() {
        // given
        User owner = createUser(true);
        User notOwner = createUser(true);
        Memo memo = createMemo(owner, "title", "content", "summary", true, true);
        MemoVersion memoVersion = memo.addVersion();

        // when
        // then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> memoVersionDomainService.removeMemoVersion(memo, notOwner, memoVersion.getId()))
                .withMessage(NOT_MEMO_OWNER.getDefaultMessage()); // 예외 메시지가 NOT_MEMO_OWNER와 같은지 확인합니다.
    }

    @Test
    @DisplayName("메모 버전 삭제 - 존재하지 않는 메모 버전을 삭제하려고 할 때 예외 발생")
void removeMemoVersionWithNotExistingMemoVersion() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", true, true);

        // when
        // then
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> memoVersionDomainService.removeMemoVersion(memo, user, UUID.randomUUID()))
                .withMessage(MEMO_VERSION_NOT_FOUND.getDefaultMessage()); // 예외 메시지가 DELETED_MEMO_VERSION와 같은지 확인합니다.
    }

    @Test
    @DisplayName("내 메모 버전 조회")
    void findMyMemoVersionTest() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", true, true);
        MemoVersion memoVersion = memo.addVersion();

        // when
        MemoVersion foundMemoVersion = memoVersionDomainService.findMyMemoVersion(memo, user, memoVersion.getId());

        // then
        assertThat(foundMemoVersion).isNotNull(); // foundMemoVersion이 null이 아닌지 확인합니다.
        assertThat(foundMemoVersion).isEqualTo(memoVersion); // foundMemoVersion이 memoVersion과 같은지 확인합니다.
    }

    @Test
    @DisplayName("내 메모 버전 조회 - 활성화되지 않은 유저가 메모 버전을 조회하려고 할 때 예외 발생")
    void findMyMemoVersionWithDisabledUser() {
        // given
        User user = createUser(false);
        Memo memo = createMemo(user, "title", "content", "summary", true, true);
        MemoVersion memoVersion = memo.addVersion();

        // when
        // then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> memoVersionDomainService.findMyMemoVersion(memo, user, memoVersion.getId()))
                .withMessage(USER_NOT_FOUND.getDefaultMessage()); // 예외 메시지가 USER_NOT_FOUND와 같은지 확인합니다.
    }

    @Test
    @DisplayName("내 메모 버전 조회 - 삭제된 메모 버전을 조회하려고 할 때 예외 발생")
    void findMyMemoVersionWithDeletedMemo() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", true, true);
        MemoVersion memoVersion = memo.addVersion();
        memo.softDelete();

        // when
        // then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> memoVersionDomainService.findMyMemoVersion(memo, user, memoVersion.getId()))
                .withMessage(DELETED_MEMO.getDefaultMessage()); // 예외 메시지가 DELETED_MEMO와 같은지 확인합니다.
    }

    @Test
    @DisplayName("내 메모 버전 조회 - 다른 유저가 메모 버전을 조회하려고 할 때 예외 발생")
    void findMyMemoVersionWithOtherUser() {
        // given
        User owner = createUser(true);
        User notOwner = createUser(true);
        Memo memo = createMemo(owner, "title", "content", "summary", true, true);
        MemoVersion memoVersion = memo.addVersion();

        // when
        // then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> memoVersionDomainService.findMyMemoVersion(memo, notOwner, memoVersion.getId()))
                .withMessage(NOT_MEMO_OWNER.getDefaultMessage()); // 예외 메시지가 NOT_MEMO_OWNER와 같은지 확인합니다.
    }

    @Test
    @DisplayName("내 메모 버전 조회 - 존재하지 않는 메모 버전을 조회하려고 할 때 예외 발생")
    void findMyMemoVersionWithNotExistingMemoVersion() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", true, true);

        // when
        // then
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> memoVersionDomainService.findMyMemoVersion(memo, user, UUID.randomUUID()))
                .withMessage(MEMO_VERSION_NOT_FOUND.getDefaultMessage()); // 예외 메시지가 DELETED_MEMO_VERSION와 같은지 확인합니다.
    }

    @Test
    @DisplayName("내 메모 버전 조회 - 삭제된 메모 버전을 조회하려고 할 때 예외 발생")
    void findMyMemoVersionWithDeletedMemoVersion() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", true, true);
        MemoVersion memoVersion = memo.addVersion();
        memoVersion.softDelete();

        // when
        // then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> memoVersionDomainService.findMyMemoVersion(memo, user, memoVersion.getId()))
                .withMessage(DELETED_MEMO_VERSION.getDefaultMessage()); // 예외 메시지가 DELETED_MEMO_VERSION와 같은지 확인합니다.
    }

    @Test
    @DisplayName("내 메모 버전 전체 조회")
    void findAllMyMemoVersionTest() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", true, true);
        MemoVersion memoVersion1 = memo.addVersion();
        MemoVersion memoVersion2 = memo.addVersion();
        MemoVersion memoVersion3 = memo.addVersion();

        // when
        var memoVersions = memoVersionDomainService.findAllMyMemoVersion(memo, user);

        // then
        assertThat(memoVersions)
                .containsExactlyInAnyOrder(memoVersion1, memoVersion2, memoVersion3); // memoVersions가 memoVersion1, memoVersion2, memoVersion3을 포함하는지 확인합니다.
    }

    @Test
    @DisplayName("내 메모 버전 전체 조회 - 삭제된 메모 버전 제외")
    void findAllMyMemoVersionWithoutDeletedMemoVersion() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", true, true);
        MemoVersion memoVersion1 = memo.addVersion();
        MemoVersion memoVersion2 = memo.addVersion();
        MemoVersion memoVersion3 = memo.addVersion();
        memoVersion2.softDelete();

        // when
        var memoVersions = memoVersionDomainService.findAllMyMemoVersion(memo, user);

        // then
        assertThat(memoVersions)
                .containsExactlyInAnyOrder(memoVersion1, memoVersion3); // memoVersions가 memoVersion1, memoVersion3을 포함하는지 확인합니다.
    }

    @Test
    @DisplayName("내 메모 버전 전체 조회 - 활성화되지 않은 유저가 메모 버전을 조회하려고 할 때 예외 발생")
    void findAllMyMemoVersionWithDisabledUser() {
        // given
        User user = createUser(false);
        Memo memo = createMemo(user, "title", "content", "summary", true, true);

        // when
        // then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> memoVersionDomainService.findAllMyMemoVersion(memo, user))
                .withMessage(USER_NOT_FOUND.getDefaultMessage()); // 예외 메시지가 USER_NOT_FOUND와 같은지 확인합니다.
    }

    @Test
    @DisplayName("내 메모 버전 전체 조회 - 삭제된 메모에 버전을 조회하려고 할 때 예외 발생")
    void findAllMyMemoVersionWithDeletedMemo() {
        // given
        User user = createUser(true);
        Memo memo = createMemo(user, "title", "content", "summary", true, true);
        memo.softDelete();

        // when
        // then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> memoVersionDomainService.findAllMyMemoVersion(memo, user))
                .withMessage(DELETED_MEMO.getDefaultMessage()); // 예외 메시지가 DELETED_MEMO와 같은지 확인합니다.
    }

    @Test
    @DisplayName("내 메모 버전 전체 조회 - 다른 유저가 메모 버전을 조회하려고 할 때 예외 발생")
    void findAllMyMemoVersionWithOtherUser() {
        // given
        User owner = createUser(true);
        User notOwner = createUser(true);
        Memo memo = createMemo(owner, "title", "content", "summary", true, true);

        // when
        // then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> memoVersionDomainService.findAllMyMemoVersion(memo, notOwner))
                .withMessage(NOT_MEMO_OWNER.getDefaultMessage()); // 예외 메시지가 NOT_MEMO_OWNER와 같은지 확인합니다.
    }

}