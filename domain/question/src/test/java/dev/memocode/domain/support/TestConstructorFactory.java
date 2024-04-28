package dev.memocode.domain.support;

import dev.memocode.domain.question.*;
import dev.memocode.domain.question.immutable.ImmutableQuestion;
import dev.memocode.domain.tag.Tag;
import dev.memocode.domain.user.ImmutableUser;
import dev.memocode.domain.user.User;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class TestConstructorFactory {

    public static User createUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .username("username")
                .enabled(true)
                .build();
    }

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

    public static Question createQuestion(User user, String title, String content) {
        return Question.builder()
                .id(UUID.randomUUID())
                .title(title)
                .deleted(false)
                .content(content)
                .user(user)
                .build();
    }

    public static ImmutableQuestion createImmutableQuestion(ImmutableUser user, String title, String content, Set<String> tags, Boolean deleted) {
        return ImmutableQuestion.builder()
                .id(UUID.randomUUID())
                .title(title)
                .content(content)
                .user(user)
                .tags(tags)
                .deleted(deleted)
                .build();
    }

    public static UpdateQuestionDomainDTO createUpdateQuestionDomainDTO(String title, String content, Set<Tag> tags) {
        return UpdateQuestionDomainDTO.builder()
                .title(title)
                .content(content)
                .tags(tags)
                .build();
    }

    public static CreateQuestionDomainDTO createCreateQuestionDomainDTO(String title, String content) {
        return CreateQuestionDomainDTO.builder()
                .title(title)
                .content(content)
                .build();
    }

    public static CreateQuestionDomainDTO createCreateQuestionDomainDTO(String title, String content, Set<Tag> tags) {
        return CreateQuestionDomainDTO.builder()
                .title(title)
                .content(content)
                .tags(tags)
                .build();
    }

    public static Tag createTag(String name) {
        return Tag.builder()
                .id(UUID.randomUUID())
                .name(name)
                .build();
    }

    public static Set<Tag> createTags(Set<String> tags) {
        return tags.stream()
                .map(tag -> Tag.builder()
                        .id(UUID.randomUUID())
                        .name(tag)
                        .build())
                .collect(Collectors.toSet());
    }

    public static CreateQuestionCommentDomainDTO createCreateQuestionCommentDomainDTO(String content) {
        return CreateQuestionCommentDomainDTO.builder()
                .content(content)
                .build();
    }

    public static UpdateQuestionCommentDomainDTO createUpdateQuestionCommentDomainDTO(String content) {
        return UpdateQuestionCommentDomainDTO.builder()
                .content(content)
                .build();
    }
}
