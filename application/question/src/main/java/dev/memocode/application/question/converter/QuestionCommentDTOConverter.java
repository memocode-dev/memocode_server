package dev.memocode.application.question.converter;

import dev.memocode.application.question.dto.FindAllQuestionComment_QuestionCommentResult;
import dev.memocode.application.question.dto.FindAllQuestionComment_UserResult;
import dev.memocode.domain.question.QuestionComment;
import dev.memocode.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuestionCommentDTOConverter {

    // TODO 비즈니스에 중요한 부분이라 이부분을 도메인 계층으로 옮겨야할 것 같음
    private final static String DELETED_QUESTION_COMMENT_CONTENT = "삭제된 댓글입니다.";

    public List<FindAllQuestionComment_QuestionCommentResult> toResult(List<QuestionComment> questionComments) {
        return questionComments.stream().map(this::toFindAllQuestionComment_QuestionCommentResult)
                .toList();
    }

    public FindAllQuestionComment_UserResult toFindAllQuestionComment_UserResult(User user) {
        return FindAllQuestionComment_UserResult.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .build();
    }

    public FindAllQuestionComment_QuestionCommentResult toFindAllQuestionComment_QuestionCommentResult(QuestionComment questionComment) {
        List<FindAllQuestionComment_QuestionCommentResult> childComments = questionComment.getChildQuestionComments().stream()
                .map(this::toFindAllQuestionComment_QuestionCommentResult)
                .toList();

        return FindAllQuestionComment_QuestionCommentResult.builder()
                .id(questionComment.getId())
                // TODO 비즈니스에 중요한 부분이라 이부분을 도메인 계층으로 옮겨야할 것 같음
                .content(questionComment.isDeleted() ? DELETED_QUESTION_COMMENT_CONTENT : questionComment.getContent())
                .user(this.toFindAllQuestionComment_UserResult(questionComment.getUser()))
                // TODO 비즈니스에 중요한 부분이라 이부분을 도메인 계층으로 옮겨야할 것 같음
                .childQuestionComments(questionComment.getParentQuestionComment() == null ? childComments : null)
                .deleted(questionComment.isDeleted())
                .createdAt(questionComment.getCreatedAt())
                .updatedAt(questionComment.getUpdatedAt())
                .build();
    }
}
