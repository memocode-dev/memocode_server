package dev.memocode.domain.support;

import dev.memocode.domain.memo.*;
import dev.memocode.domain.user.ImmutableUser;
import dev.memocode.domain.user.User;

import java.time.Instant;
import java.util.UUID;

public class TestConstructorFactory {

    public static User createUser(Boolean enabled) {
        return User.builder()
                .id(UUID.randomUUID())
                .username("username")
                .enabled(enabled)
                .build();
    }

    public static ImmutableUser createImmutableUser() {
        return ImmutableUser.builder()
                .id(UUID.randomUUID())
                .username("username")
                .enabled(true)
                .build();
    }

    public static User immutableUserToUser(ImmutableUser immutableUser) {
        return User.builder()
                .id(immutableUser.getId())
                .username(immutableUser.getUsername())
                .enabled(immutableUser.getEnabled())
                .build();
    }

    public static MemoCreateDomainDTO createMemoCreateDomainDTO(User user, String title, String content, String summary, Boolean security) {
        return MemoCreateDomainDTO.builder()
                .title(title)
                .content(content)
                .summary(summary)
                .security(security)
                .user(user)
                .build();
    }

    public static Memo createMemo(User user, String title, String content, String summary, Boolean security, Boolean visibility) {
        return Memo.builder()
                .id(UUID.randomUUID())
                .title(title)
                .content(content)
                .summary(summary)
                .visibility(visibility)
                .security(security)
                .bookmarked(false)
                .user(user)
                .deleted(false)
                .build();
    }

    public static Memo createMemo(User user, String title, String content, String summary, Boolean security, Instant visibilityAchievedAt) {
        return Memo.builder()
                .id(UUID.randomUUID())
                .title(title)
                .content(content)
                .summary(summary)
                .visibility(false)
                .visibilityAchievedAt(visibilityAchievedAt)
                .security(security)
                .bookmarked(false)
                .user(user)
                .deleted(false)
                .build();
    }

    public static MemoUpdateDomainDTO createMemoUpdateDomainDTO(String title, String content, String summary, Boolean security, Boolean visibility, Boolean bookmarked) {
        return MemoUpdateDomainDTO.builder()
                .title(title)
                .content(content)
                .summary(summary)
                .security(security)
                .visibility(visibility)
                .bookmarked(bookmarked)
                .build();
    }

    public static ImmutableMemo createImmutableMemo(ImmutableUser user, String title, String content, String summary, Boolean visibility, Boolean deleted) {
        ImmutableMemo immutableMemo = ImmutableMemo.builder()
                .id(UUID.randomUUID())
                .title(title)
                .summary(summary)
                .content(content)
                .visibility(visibility)
                .user(user)
                .formattedMemo(null)
                .deleted(deleted)
                .build();
        return ImmutableMemo.builder()
                .id(UUID.randomUUID())
                .title(title)
                .summary(summary)
                .content(content)
                .visibility(visibility)
                .user(user)
                .formattedMemo(immutableMemo)
                .deleted(deleted)
                .build();
    }

    public static CreateMemoCommentDomainDTO createCreateMemoCommentDomainDTO(String content) {
        return CreateMemoCommentDomainDTO.builder()
                .content(content)
                .build();
    }

    public static UpdateMemoCommentDomainDTO createUpdateMemoCommentDomainDTO(String content) {
        return UpdateMemoCommentDomainDTO.builder()
                .content(content)
                .build();
    }

}
