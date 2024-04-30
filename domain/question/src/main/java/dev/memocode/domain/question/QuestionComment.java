package dev.memocode.domain.question;

import dev.memocode.domain.core.BaseEntity;
import dev.memocode.domain.core.ForbiddenException;
import dev.memocode.domain.core.SoftDeleteBaseEntity;
import dev.memocode.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

import static dev.memocode.domain.question.QuestionDomainErrorCode.DELETED_QUESTION_COMMENT;
import static dev.memocode.domain.question.QuestionDomainErrorCode.NOT_QUESTION_COMMENT_OWNER;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor(access = PROTECTED)
@Table(name = "question_comments")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class QuestionComment extends SoftDeleteBaseEntity {

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_question_comment_id")
    private QuestionComment parentQuestionComment;

    @OneToMany(mappedBy = "parentQuestionComment")
    @Builder.Default
    private List<QuestionComment> childQuestionComments = new ArrayList<>();

    protected void changeContent(String content) {
        this.content = content == null ? this.content : content;
    }

    public void softDelete() {
        super.delete();
    }

    public void addChildComment(QuestionComment memoComment) {
        this.childQuestionComments.add(memoComment);
    }

    public void assertIsNotDeleted() {
        if (this.getDeleted()) {
            throw new ForbiddenException(DELETED_QUESTION_COMMENT);
        }
    }

    public void assertIsQuestionCommentOwner(User user) {
        if (!this.getUser().equals(user)) {
            throw new ForbiddenException(NOT_QUESTION_COMMENT_OWNER);
        }
    }
}
