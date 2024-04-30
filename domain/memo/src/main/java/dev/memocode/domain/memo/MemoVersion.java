package dev.memocode.domain.memo;

import dev.memocode.domain.core.BaseEntity;
import dev.memocode.domain.core.ForbiddenException;
import dev.memocode.domain.core.SoftDeleteBaseEntity;
import dev.memocode.domain.user.User;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static dev.memocode.domain.memo.MemoDomainErrorCode.DELETED_MEMO_VERSION;
import static dev.memocode.domain.memo.MemoDomainErrorCode.NOT_MEMO_VERSION_OWNER;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor(access = PROTECTED)
@Table(name = "memo_version")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class MemoVersion extends SoftDeleteBaseEntity {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "memo_id")
    private Memo memo;

    @Column(name = "content")
    private String content;

    @Column(name = "version")
    private Integer version;

    protected void softDelete() {
        super.delete();
    }

    protected void assertIsMemoVersionOwner(User user) {
        if (!this.memo.getUser().equals(user)) {
            throw new ForbiddenException(NOT_MEMO_VERSION_OWNER);
        }
    }

    protected void assertIsNotDeleted() {
        if (this.getDeleted()) {
            throw new ForbiddenException(DELETED_MEMO_VERSION);
        }
    }
}
