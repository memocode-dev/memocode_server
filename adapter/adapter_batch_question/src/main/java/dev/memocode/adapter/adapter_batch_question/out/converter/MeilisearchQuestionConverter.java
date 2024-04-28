package dev.memocode.adapter.adapter_batch_question.out.converter;

import dev.memocode.adapter.adapter_batch_core.MeilisearchUser;
import dev.memocode.adapter.adapter_batch_question.out.dto.MeilisearchQuestion;
import dev.memocode.domain.question.Question;
import dev.memocode.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MeilisearchQuestionConverter {

    public MeilisearchQuestion toMeilisearchQuestion(Question question) {
        User user = question.getUser();
        MeilisearchUser meilisearchUser = MeilisearchUser.builder()
                .id(user.getId())
                .username(user.getUsername())
                .enabled(user.getEnabled())
                .build();

        Set<String> tags = question.getQuestionTags().stream()
                .map(questionTag -> questionTag.getTag().getName())
                .collect(Collectors.toSet());

        return MeilisearchQuestion.builder()
                .id(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .userId(user.getId())
                .user(meilisearchUser)
                .tags(tags)
                .createdAt(question.getCreatedAt())
                .updatedAt(question.getUpdatedAt())
                .deletedAt(question.getDeletedAt())
                .deleted(question.getDeleted())
                .build();
    }
}
