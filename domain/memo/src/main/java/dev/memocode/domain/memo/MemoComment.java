package dev.memocode.domain.memo;

import dev.memocode.domain.core.BaseEntity;
import dev.memocode.domain.core.ForbiddenException;
import dev.memocode.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

import static dev.memocode.domain.memo.MemoDomainErrorCode.DELETED_MEMO_COMMENT;
import static dev.memocode.domain.memo.MemoDomainErrorCode.NOT_MEMO_COMMENT_OWNER;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor(access = PROTECTED)
@Table(name = "memo_comments")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class MemoComment extends BaseEntity {

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "memo_id")
    private Memo memo;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_memo_comment_id")
    private MemoComment parentMemoComment;

    @OneToMany(mappedBy = "parentMemoComment", cascade = {CascadeType.PERSIST})
    @Builder.Default
    private List<MemoComment> childMemoComments = new ArrayList<>();

    protected void softDelete() {
        // 메모의 삭제로 인한 cascade 삭제일 경우 메모 댓글의 자식 댓글들도 삭제
        if (this.memo.isDeleted()) {
            super.delete();
            childMemoComments.forEach(MemoComment::softDelete);
            return;
        }

        super.delete();
    }

    protected void changeContent(String content) {
        this.content = content == null ? this.content : content;
    }

    protected void assertIsMemoCommentOwner(User user) {
        if (!this.user.equals(user)) {
            throw new ForbiddenException(NOT_MEMO_COMMENT_OWNER);
        }
    }

    protected void assertIsNotDeleted() {
        if (this.isDeleted()) {
            throw new ForbiddenException(DELETED_MEMO_COMMENT);
        }
    }

    protected void addChildComment(MemoComment childMemoComment) {
        this.childMemoComments.add(childMemoComment);
    }
}
