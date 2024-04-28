package dev.memocode.domain.question;

import dev.memocode.domain.core.BaseEntity;
import dev.memocode.domain.core.ForbiddenException;
import dev.memocode.domain.tag.Tag;
import dev.memocode.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.*;

import static dev.memocode.domain.question.QuestionDomainErrorCode.DELETED_QUESTION;
import static dev.memocode.domain.question.QuestionDomainErrorCode.NOT_QUESTION_OWNER;
import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor(access = PROTECTED)
@Table(name = "questions")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Question extends BaseEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = LAZY)
    private User user;

    @OneToMany(mappedBy = "question", cascade = {PERSIST, MERGE})
    @Builder.Default
    private Set<QuestionTag> questionTags = new HashSet<>();

    @OneToMany(mappedBy = "question", cascade = {PERSIST})
    @Builder.Default
    private List<QuestionComment> questionComments = new ArrayList<>();

    /**
     * 질문의 제목, 내용 그리고 태그들을 변경합니다.
     *  - 해당 필드가 null인 경우에는 변경하지 않습니다.
     * @param dto 변경하고자 하는 필드의 값들
     */
    protected void change(UpdateQuestionDomainDTO dto) {
        String title = dto.getTitle();
        String content = dto.getContent();
        Set<Tag> tags = dto.getTags();

        this.title = title != null ? title : this.title;
        this.content = content != null ? content : this.content;

        if (tags != null) {
            this.updateTags(tags);
        }
    }

    /**
     * 질문의 태그들을 업데이트합니다.
     *  - 현재 매개변수로 주어진 태그들만 질문 태그에 존재하게 됩니다.
     * @param updatedTags 업데이트하고자 하는 태그들
     */
    protected void updateTags(Set<Tag> updatedTags) {
        // updatedTags에 포함되지 않은 질문태그들은 소프트삭제합니다.
        this.questionTags.stream()
                .filter(questionTag -> !updatedTags.contains(questionTag.getTag()))
                .forEach(QuestionTag::softDelete);

        // updatedTags에 포함된 질문태그들 중에서 삭제된 질문태그들은 다시 복구합니다.
        this.questionTags.stream()
                .filter(questionTag -> updatedTags.contains(questionTag.getTag()))
                .filter(BaseEntity::getDeleted)
                .forEach(QuestionTag::recover);

        // question과 tag를 통해 동등성을 체크하므로 이미 포함되어있는 questionTag는 추가되지 않고 새로운 questionTag만 생성됨
        updatedTags.forEach(tag -> {
            QuestionTag questionTag = QuestionTag.builder()
                    .id(UUID.randomUUID())
                    .question(this)
                    .tag(tag)
                    .build();

            this.questionTags.add(questionTag);
        });

        // 태그 업데이트시에 메모 updatedAt 업데이트
        super.updateUpdatedAt();
    }

    /**
     * 자신이 소유한 질문이 맞는지 확인하고 자신이 소유한 질문이 아니면 exception이 발생합니다.
     * @param user 자신의 질문임을 확인하고자 하는 유저
     */
    protected void assertIsQuestionOwner(User user) {
        if (!this.getUser().equals(user)) {
            throw new ForbiddenException(NOT_QUESTION_OWNER);
        }
    }

    /**
     * 질문이 삭제되지 않았는지 확인합니다.
     *  - 삭제되지 않았다면 통과, 삭제되었다면 exception 발생
     */
    protected void assertIsNotDeleted() {
        if (this.getDeleted()) {
            throw new ForbiddenException(DELETED_QUESTION);
        }
    }

    /**
     * 질문을 소프트삭제합니다.
     *  - 연관된 questionTag들은 모두 소프트삭제합니다.
     */
    protected void softDelete() {
        super.delete();

        this.questionTags.forEach(QuestionTag::softDelete);
    }

    /**
     * 질문 댓글을 추가합니다.
     * @param content 변경하고자 하는 질문 댓글 내용
     * @param user 질문 댓글을 생성하고자 하는 유저
     * @return 생성된 질문댓글
     */
    protected QuestionComment addComment(String content, User user) {
        QuestionComment questionComment = QuestionComment.builder()
                .id(UUID.randomUUID())
                .question(this)
                .user(user)
                .content(content)
                .build();

        this.questionComments.add(questionComment);

        return questionComment;
    }

    /**
     * 질문 댓글의 내용을 변경합니다.
     * @param questionComment 내용을 변경하고자 하는 질문 댓글
     * @param content 변경하고자 하는 내용
     */
    protected void updateComment(QuestionComment questionComment, String content) {
        questionComment.changeContent(content);
    }

    /**
     * 질문 댓글을 소프트 삭제합니다.
     * @param questionComment 삭제하고자 하는 질문 댓글
     */
    protected void removeComment(QuestionComment questionComment) {
        questionComment.softDelete();
    }

    /**
     * 질문댓글의 질문답글을 생성합니다.
     * @param parentQuestionComment 답글을 작성하고자 하는 댓글
     * @param user 작성자
     * @param content 답글 내용
     * @return 답글 반환
     */
    protected QuestionComment addChildComment(QuestionComment parentQuestionComment, User user, String content) {
        QuestionComment childQuestionComment = QuestionComment.builder()
                .id(UUID.randomUUID())
                .content(content)
                .question(this)
                .parentQuestionComment(parentQuestionComment)
                .user(user)
                .build();
        parentQuestionComment.addChildComment(childQuestionComment);
        return childQuestionComment;
    }
}
