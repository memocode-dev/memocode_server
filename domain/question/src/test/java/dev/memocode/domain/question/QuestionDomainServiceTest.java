package dev.memocode.domain.question;

import dev.memocode.domain.core.ForbiddenException;
import dev.memocode.domain.question.immutable.ImmutableQuestion;
import dev.memocode.domain.tag.Tag;
import dev.memocode.domain.user.ImmutableUser;
import dev.memocode.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

import static dev.memocode.domain.question.QuestionDomainErrorCode.DELETED_QUESTION;
import static dev.memocode.domain.question.QuestionDomainErrorCode.NOT_QUESTION_OWNER;
import static dev.memocode.domain.support.TestConstructorFactory.*;
import static dev.memocode.domain.user.UserDomainErrorCode.USER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
@ContextConfiguration(classes = {QuestionDomainService.class}) // 테스트할 클래스를 지정해줍니다.
@ExtendWith(SpringExtension.class) // junit5와 스프링을 연동해주는 어노테이션
class QuestionDomainServiceTest {

    @Autowired private QuestionDomainService questionDomainService;

    @Test
    @DisplayName("질문 생성 테스트")
    void createQuestionTest() {
        //given
        Set<Tag> tags = createTags(Set.of("tag1", "tag2"));
        User user = createUser();
        CreateQuestionDomainDTO createQuestionDomainDTO = createCreateQuestionDomainDTO("title", "content", tags);
        //when
        Question question = questionDomainService.createQuestion(user, createQuestionDomainDTO);
        //then
        assertThat(question.getId()).isNotNull(); // ID는 null이 아니어야 합니다.
        assertThat(question.getTitle()).isEqualTo("title"); // 제목이 입력한 제목과 일치해야 합니다.
        assertThat(question.getContent()).isEqualTo("content"); // 내용이 입력한 내용과 일치해야 합니다.
        assertThat(question.getUser()).isEqualTo(user); // 사용자가 입력한 사용자와 일치해야 합니다.
        assertThat(question.getQuestionTags())
                .extracting(QuestionTag::getTag)
                .allMatch(tags::contains); // 태그가 입력한 태그와 일치해야 합니다.
    }

    @Test
    @DisplayName("질문 수정 테스트")
    void updateQuestionTest() {
        //given
        Set<Tag> tags = createTags(Set.of("tag1", "tag2"));
        User user = createUser();
        Question question = createQuestion(user, "title", "content");
        UpdateQuestionDomainDTO updateQuestionDomainDTO = createUpdateQuestionDomainDTO("updated title", "updated content", tags);
        //when
        Question updatedQuestion = questionDomainService.updateQuestion(question, user, updateQuestionDomainDTO);
        //then
        assertThat(updatedQuestion.getTitle()).isEqualTo("updated title"); // 제목이 수정된 제목과 일치해야 합니다.
        assertThat(updatedQuestion.getContent()).isEqualTo("updated content"); // 내용이 수정된 내용과 일치해야 합니다.
        assertThat(updatedQuestion.getUser()).isEqualTo(user); // 사용자가 수정한 사용자와 일치해야 합니다.
        assertThat(updatedQuestion.getQuestionTags())
                .extracting(QuestionTag::getTag)
                .allMatch(tags::contains); // 태그가 수정된 태그와 일치해야 합니다.
    }

    @Test
    @DisplayName("질문 수정 테스트 - 활성화된 유저가 아닐 경우")
    void updateQuestionTest_NotEnabledUser() {
        //given
        Set<Tag> tags = createTags(Set.of("tag1", "tag2"));
        User user = createUser(false);
        Question question = createQuestion(user, "title", "content");
        UpdateQuestionDomainDTO updateQuestionDomainDTO = createUpdateQuestionDomainDTO("updated title", "updated content", tags);
        //when
        //then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> questionDomainService.updateQuestion(question, user, updateQuestionDomainDTO))
                .withMessage(USER_NOT_FOUND.getDefaultMessage()); // 활성화된 유저가 아닌 경우 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("질문 수정 테스트 - 삭제된 질문인 경우")
    void updateQuestionTest_DeletedQuestion() {
        //given
        Set<Tag> tags = createTags(Set.of("tag1", "tag2"));
        User user = createUser();
        Question question = createQuestion(user, "title", "content");
        question.softDelete();
        UpdateQuestionDomainDTO updateQuestionDomainDTO = createUpdateQuestionDomainDTO("updated title", "updated content", tags);
        //when
        //then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> questionDomainService.updateQuestion(question, user, updateQuestionDomainDTO))
                .withMessage(DELETED_QUESTION.getDefaultMessage()); // 삭제된 질문인 경우 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("질문 수정 테스트 - 사용자가 질문의 소유자가 아닌 경우")
    void updateQuestionTest_NotQuestionOwner() {
        //given
        Set<Tag> tags = createTags(Set.of("tag1", "tag2"));
        User user = createUser();
        User notOwner = createUser();
        Question question = createQuestion(user, "title", "content");
        UpdateQuestionDomainDTO updateQuestionDomainDTO = createUpdateQuestionDomainDTO("updated title", "updated content", tags);
        //when
        //then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> questionDomainService.updateQuestion(question, notOwner, updateQuestionDomainDTO))
                .withMessage(NOT_QUESTION_OWNER.getDefaultMessage()); // 질문의 소유자가 아닌 경우 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("질문 삭제 테스트")
    void deleteQuestionTest() {
        //given
        User user = createUser();
        Question question = createQuestion(user, "title", "content");
        //when
        questionDomainService.deleteQuestion(question, user);
        //then
        assertThat(question.getDeleted()).isTrue(); // 삭제된 질문인 경우 true여야 합니다.
    }

    @Test
    @DisplayName("질문 삭제 테스트 - 활성화된 유저가 아닐 경우")
    void deleteQuestionTest_NotEnabledUser() {
        //given
        User user = createUser(false);
        Question question = createQuestion(user, "title", "content");
        //when
        //then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> questionDomainService.deleteQuestion(question, user))
                .withMessage(USER_NOT_FOUND.getDefaultMessage()); // 활성화된 유저가 아닌 경우 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("질문 삭제 테스트 - 삭제된 질문인 경우")
    void deleteQuestionTest_DeletedQuestion() {
        //given
        User user = createUser();
        Question question = createQuestion(user, "title", "content");
        question.softDelete();
        //when
        //then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> questionDomainService.deleteQuestion(question, user))
                .withMessage(DELETED_QUESTION.getDefaultMessage()); // 삭제된 질문인 경우 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("질문 삭제 테스트 - 사용자가 질문의 소유자가 아닌 경우")
    void deleteQuestionTest_NotQuestionOwner() {
        //given
        User user = createUser();
        User notOwner = createUser();
        Question question = createQuestion(user, "title", "content");
        //when
        //then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> questionDomainService.deleteQuestion(question, notOwner))
                .withMessage(NOT_QUESTION_OWNER.getDefaultMessage()); // 질문의 소유자가 아닌 경우 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("질문 조회 테스트")
    void findQuestionTest() {
        //given
        User user = createUser();
        Question question = createQuestion(user, "title", "content");
        //when
        Question foundQuestion = questionDomainService.findQuestion(question);
        //then
        assertThat(foundQuestion).isEqualTo(question); // 조회된 질문이 입력한 질문과 일치해야 합니다.
    }

    @Test
    @DisplayName("질문 조회 테스트 - 삭제된 질문인 경우")
    void findQuestionTest_DeletedQuestion() {
        //given
        User user = createUser();
        Question question = createQuestion(user, "title", "content");
        question.softDelete();
        //when
        //then
        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> questionDomainService.findQuestion(question))
                .withMessage(DELETED_QUESTION.getDefaultMessage()); // 삭제된 질문인 경우 예외가 발생해야 합니다.
    }

    @Test
    @DisplayName("질문 검색 테스트")
    void searchQuestionTest() {
        //given
        ImmutableUser user = createImmutableUser();
        ImmutableQuestion question1 = createImmutableQuestion(user, "title1", "content1", Set.of("tag1", "tag2"), false);
        ImmutableQuestion question2 = createImmutableQuestion(user, "title2", "content2", Set.of("tag1", "tag2"), true);
        //when
        List<ImmutableQuestion> searchedQuestion = questionDomainService.searchQuestion(List.of(question1, question2));
        //then
        assertThat(searchedQuestion.size()).isEqualTo(1); // 삭제되지 않은 질문만 검색되어야 합니다.
        assertThat(searchedQuestion.get(0)).isEqualTo(question1); // 삭제되지 않은 질문만 검색되어야 합니다.
    }
}