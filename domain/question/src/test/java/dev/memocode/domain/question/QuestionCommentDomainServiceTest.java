package dev.memocode.domain.question;

import dev.memocode.domain.core.ForbiddenException;
import dev.memocode.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Objects;

import static dev.memocode.domain.question.QuestionDomainErrorCode.*;
import static dev.memocode.domain.support.TestConstructorFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ContextConfiguration(classes = {QuestionCommentDomainService.class}) // 테스트할 클래스를 지정해줍니다.
@ExtendWith(SpringExtension.class) // junit5와 스프링을 연동해주는 어노테이션
class QuestionCommentDomainServiceTest {

    @Autowired private QuestionCommentDomainService questionCommentDomainService;

    @Test
    @DisplayName("질문 댓글 생성 테스트")
    void createQuestionCommentTest() {
        //given
        User user = createUser();
        Question question = createQuestion(user, "title", "content");
        CreateQuestionCommentDomainDTO createQuestionCommentDomainDTO = createCreateQuestionCommentDomainDTO("content");
        //when
        QuestionComment questionComment = questionCommentDomainService.createQuestionComment(question, user, createQuestionCommentDomainDTO);
        //then
        assertThat(questionComment.getId()).isNotNull(); // ID는 null이 아니어야 합니다.
        assertThat(questionComment.getContent()).isEqualTo("content"); // 내용이 입력한 내용과 일치해야 합니다.
        assertThat(questionComment.getUser()).isEqualTo(user); // 사용자가 입력한 사용자와 일치해야 합니다.
    }

    @Test
    @DisplayName("질문 댓글 생성 테스트 - 삭제된 질문인 경우")
    void createQuestionCommentTest_DeletedQuestion() {
        //given
        User user = createUser();
        Question question = createQuestion(user, "title", "content");
        question.softDelete();
        CreateQuestionCommentDomainDTO createQuestionCommentDomainDTO = createCreateQuestionCommentDomainDTO("content");
        //when
        //then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> questionCommentDomainService.createQuestionComment(question, user, createQuestionCommentDomainDTO))
                .withMessage(DELETED_QUESTION.getDefaultMessage()); // 삭제된 질문인 경우 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("질문 댓글 수정 테스트")
    void updateQuestionCommentTest() {
        //given
        User user = createUser();
        Question question = createQuestion(user, "title", "content");
        QuestionComment questionComment = question.addComment("comment", user);
        UpdateQuestionCommentDomainDTO updateQuestionCommentDomainDTO = createUpdateQuestionCommentDomainDTO("updated content");
        //when
        questionCommentDomainService.updateQuestionComment(question, questionComment, user, updateQuestionCommentDomainDTO);
        //then
        assertThat(questionComment.getContent()).isEqualTo("updated content"); // 내용이 수정된 내용과 일치해야 합니다.
    }

    @Test
    @DisplayName("질문 댓글 수정 테스트 - 삭제된 질문인 경우")
    void updateQuestionCommentTest_DeletedQuestion() {
        //given
        User user = createUser();
        Question question = createQuestion(user, "title", "content");
        QuestionComment questionComment = question.addComment("comment", user);
        question.softDelete();
        UpdateQuestionCommentDomainDTO updateQuestionCommentDomainDTO = createUpdateQuestionCommentDomainDTO("updated content");
        //when
        //then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> questionCommentDomainService.updateQuestionComment(question, questionComment, user, updateQuestionCommentDomainDTO))
                .withMessage(DELETED_QUESTION.getDefaultMessage()); // 삭제된 질문인 경우 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("질문 댓글 수정 테스트 - 삭제된 댓글인 경우")
    void updateQuestionCommentTest_DeletedQuestionComment() {
        //given
        User user = createUser();
        Question question = createQuestion(user, "title", "content");
        QuestionComment questionComment = question.addComment("comment", user);
        questionComment.softDelete();
        UpdateQuestionCommentDomainDTO updateQuestionCommentDomainDTO = createUpdateQuestionCommentDomainDTO("updated content");
        //when
        //then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> questionCommentDomainService.updateQuestionComment(question, questionComment, user, updateQuestionCommentDomainDTO))
                .withMessage(DELETED_QUESTION_COMMENT.getDefaultMessage()); // 삭제된 댓글인 경우 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("질문 댓글 수정 테스트 - 댓글의 소유자가 아닌 경우")
    void updateQuestionCommentTest_NotOwner() {
        //given
        User user = createUser();
        User not_owner = createUser();
        Question question = createQuestion(user, "title", "content");
        QuestionComment questionComment = question.addComment("comment", user);
        UpdateQuestionCommentDomainDTO updateQuestionCommentDomainDTO = createUpdateQuestionCommentDomainDTO("updated content");
        //when
        //then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> questionCommentDomainService.updateQuestionComment(question, questionComment, not_owner, updateQuestionCommentDomainDTO))
                .withMessage(NOT_QUESTION_COMMENT_OWNER.getDefaultMessage()); // 댓글의 소유자가 아닌 경우 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("질문 댓글 삭제 테스트")
    void deleteQuestionCommentTest() {
        //given
        User user = createUser();
        Question question = createQuestion(user, "title", "content");
        QuestionComment questionComment1 = question.addComment("comment1", user);
        QuestionComment questionComment2 = question.addComment("comment2", user);
        //when
        questionCommentDomainService.deleteQuestionComment(question, questionComment1, user);
        //then
        assertThat(questionComment1.getDeleted()).isTrue(); // 삭제된 댓글인 경우 true여야 합니다.
        assertThat(questionComment2.getDeleted()).isFalse(); // 삭제되지않은 댓글인 경우 false여야 합니다.
    }

    @Test
    @DisplayName("질문 댓글 삭제 테스트 - 삭제된 질문인 경우")
    void deleteQuestionCommentTest_DeletedQuestion() {
        //given
        User user = createUser();
        Question question = createQuestion(user, "title", "content");
        QuestionComment questionComment = question.addComment("comment", user);
        question.softDelete();
        //when
        //then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> questionCommentDomainService.deleteQuestionComment(question, questionComment, user))
                .withMessage(DELETED_QUESTION.getDefaultMessage()); // 삭제된 질문인 경우 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("질문 댓글 삭제 테스트 - 삭제된 댓글인 경우")
    void deleteQuestionCommentTest_DeletedQuestionComment() {
        //given
        User user = createUser();
        Question question = createQuestion(user, "title", "content");
        QuestionComment questionComment = question.addComment("comment", user);
        questionComment.softDelete();
        //when
        //then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> questionCommentDomainService.deleteQuestionComment(question, questionComment, user))
                .withMessage(DELETED_QUESTION_COMMENT.getDefaultMessage()); // 삭제된 댓글인 경우 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("질문 댓글 삭제 테스트 - 댓글의 소유자가 아닌 경우")
    void deleteQuestionCommentTest_NotOwner() {
        //given
        User user = createUser();
        User notOwner = createUser();
        Question question = createQuestion(user, "title", "content");
        QuestionComment questionComment = question.addComment("comment", user);
        //when
        //then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> questionCommentDomainService.deleteQuestionComment(question, questionComment, notOwner))
                .withMessage(NOT_QUESTION_COMMENT_OWNER.getDefaultMessage()); // 댓글의 소유자가 아닌 경우 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("질문 댓글 조회 테스트")
    void findAllTest() {
        //given
        User user = createUser();
        Question question = createQuestion(user, "title", "content");
        QuestionComment questionComment = question.addComment("comment", user);
        //when
        //then
        assertThat(questionCommentDomainService.findAll(question)).contains(questionComment); // 조회된 댓글 목록에 댓글이 포함되어야 합니다.
    }

    @Test
    @DisplayName("질문 댓글 조회 테스트 - 삭제된 질문인 경우")
    void findAllTest_DeletedQuestion() {
        //given
        User user = createUser();
        Question question = createQuestion(user, "title", "content");
        question.addComment("comment", user);
        question.softDelete();
        //when
        //then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> questionCommentDomainService.findAll(question))
                .withMessage(DELETED_QUESTION.getDefaultMessage()); // 삭제된 질문인 경우 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("질문 댓글 조회 테스트 - 삭제된 댓글인 경우")
    void findAllTest_DeletedQuestionComment() {
        //given
        User user = createUser();
        Question question = createQuestion(user, "title", "content");
        QuestionComment questionComment_1 = question.addComment("comment_1", user);
        QuestionComment questionComment_2 = question.addComment("comment_2", user);
        questionComment_1.softDelete();
        //when
        List<QuestionComment> questionCommentList = questionCommentDomainService.findAll(question);
        //then
        assertThat(Objects.requireNonNull(questionCommentList.stream()
                        .filter(c -> c.equals(questionComment_1))
                        .findFirst()
                        .orElse(null))
                .getDeleted()).isTrue(); // 삭제된 댓글인 경우 true여야 합니다.

        assertThat(Objects.requireNonNull(questionCommentList.stream()
                        .filter(c -> c.equals(questionComment_2))
                        .findFirst()
                        .orElse(null))
                .getDeleted()).isFalse(); // 삭제되지 않은 댓글인 경우 false여야 합니다.
    }

    @Test
    @DisplayName("질문 대댓글 생성 테스트")
    void createChildQuestionCommentTest() {
        //given
        User user = createUser();
        Question question = createQuestion(user, "title", "content");
        QuestionComment parentQuestionComment = question.addComment("parentQuestionComment", user);
        CreateQuestionCommentDomainDTO createQuestionCommentDomainDTO = createCreateQuestionCommentDomainDTO("childQuestionComment");
        //when
        QuestionComment childQuestionComment = questionCommentDomainService.createChildQuestionComment(question, parentQuestionComment, user, createQuestionCommentDomainDTO);
        //then
        assertThat(childQuestionComment.getId()).isNotNull(); // ID는 null이 아니어야 합니다.
        assertThat(childQuestionComment.getContent()).isEqualTo("childQuestionComment"); // 내용이 입력한 내용과 일치해야 합니다.
        assertThat(childQuestionComment.getUser()).isEqualTo(user); // 사용자가 입력한 사용자와 일치해야 합니다.
        assertThat(childQuestionComment.getParentQuestionComment()).isEqualTo(parentQuestionComment); // 부모 댓글이 입력한 부모 댓글과 일치해야 합니다.
    }

    @Test
    @DisplayName("질문 대댓글 삭제 테스트")
    void deleteChildQuestionCommentTest() {
        //given
        User user = createUser();
        Question question = createQuestion(user, "title", "content");
        QuestionComment parentQuestionComment = question.addComment("parentQuestionComment", user);
        QuestionComment childQuestionComment = question.addChildComment(parentQuestionComment, user, "childQuestionComment");
        //when
        questionCommentDomainService.deleteQuestionComment(question, childQuestionComment, user);
        //then
        assertThat(childQuestionComment.getDeleted()).isTrue(); // 삭제된 자식댓글인 경우 true여야 합니다.
        assertThat(parentQuestionComment.getDeleted()).isFalse(); // 부모댓글인 경우 false여야 합니다.
    }


}