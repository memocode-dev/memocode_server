package dev.memocode.domain.question;

import dev.memocode.domain.user.User;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Service
@Validated
public class QuestionDomainService {

    public Question createQuestion(User user, @Valid CreateQuestionDomainDTO dto) {
        Question question = Question.builder()
                .id(UUID.randomUUID())
                .title(dto.getTitle())
                .content(dto.getContent())
                .user(user)
                .build();

        question.updateTags(dto.getTags());

        return question;
    }

    public Question updateQuestion(Question question, User user, @Valid UpdateQuestionDomainDTO dto) {
        question.assertIsNotDeleted();
        question.assertIsQuestionOwner(user);

        question.change(dto);

        return question;
    }

    public void deleteQuestion(Question question, User user) {
        question.assertIsNotDeleted();
        question.assertIsQuestionOwner(user);

        question.softDelete();
    }

    public Question findQuestion(Question question) {
        question.assertIsNotDeleted();
        return question;
    }

    public List<Question> searchQuestion(List<Question> questions) {
        return questions.stream()
                .filter(question -> !question.isDeleted())
                .toList();
    }
}
