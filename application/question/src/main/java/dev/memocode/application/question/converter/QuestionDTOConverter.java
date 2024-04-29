package dev.memocode.application.question.converter;

import dev.memocode.application.question.dto.FindQuestion_QuestionResult;
import dev.memocode.application.question.dto.FindQuestion_UserResult;
import dev.memocode.application.question.dto.SearchQuestion_QuestionResult;
import dev.memocode.application.question.dto.SearchQuestion_UserResult;
import dev.memocode.domain.question.Question;
import dev.memocode.domain.question.QuestionTag;
import dev.memocode.domain.question.immutable.ImmutableQuestion;
import dev.memocode.domain.user.ImmutableUser;
import dev.memocode.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class QuestionDTOConverter {
    
    public FindQuestion_QuestionResult toFindQuestion_QuestionResult(Question question) {
        return FindQuestion_QuestionResult.builder()
                .id(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .tags(this.toTagStrings(question.getQuestionTags()))
                .createdAt(question.getCreatedAt())
                .updatedAt(question.getUpdatedAt())
                .user(toFindQuestion_UserResult(question.getUser()))
                .build();
    }

    public FindQuestion_UserResult toFindQuestion_UserResult(User user) {
        return FindQuestion_UserResult.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .build();
    }

    public List<SearchQuestion_QuestionResult> toSearchQuestion_QuestionResult(List<ImmutableQuestion> questions) {
        return questions.stream()
                .map(this::toSearchQuestion_QuestionResult)
                .toList();
    }

    public SearchQuestion_UserResult toSearchQuestion_UserResult(ImmutableUser user) {
        return SearchQuestion_UserResult.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }

    public SearchQuestion_QuestionResult toSearchQuestion_QuestionResult(ImmutableQuestion question) {
        return SearchQuestion_QuestionResult.builder()
                .id(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .tags(question.getTags())
                .createdAt(question.getCreatedAt())
                .updatedAt(question.getUpdatedAt())
                .user(toSearchQuestion_UserResult(question.getUser()))
                .build();
    }

    private Set<String> toTagStrings(Set<QuestionTag> questionTags) {
        return questionTags.stream()
                .map(questionTag -> questionTag.getTag().getName())
                .collect(Collectors.toSet());
    }
}
