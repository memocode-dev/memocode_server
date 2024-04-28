package dev.memocode.domain.question;

import dev.memocode.domain.user.User;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class QuestionCommentDomainService {
    public QuestionComment createQuestionComment(Question question, User user, @Valid CreateQuestionCommentDomainDTO dto) {
        question.assertIsNotDeleted();
        return question.addComment(dto.getContent(), user);
    }

    public void updateQuestionComment(
            Question question, QuestionComment questionComment, User user, @Valid UpdateQuestionCommentDomainDTO dto) {
        question.assertIsNotDeleted();
        questionComment.assertIsNotDeleted();
        questionComment.assertIsQuestionCommentOwner(user);
        question.updateComment(questionComment, dto.getContent());
    }

    public void deleteQuestionComment(Question question, QuestionComment questionComment, User user) {
        question.assertIsNotDeleted();
        questionComment.assertIsNotDeleted();
        questionComment.assertIsQuestionCommentOwner(user);
        question.removeComment(questionComment);
    }

    public List<QuestionComment> findAll(Question question) {
        question.assertIsNotDeleted();
        return question.getQuestionComments();
    }

    public QuestionComment createChildQuestionComment(
            Question question, QuestionComment questionComment, User user, @Valid CreateQuestionCommentDomainDTO dto) {
        question.assertIsNotDeleted();
        questionComment.assertIsNotDeleted();
        return question.addChildComment(questionComment, user, dto.getContent());
    }
}
