package dev.memocode.memo_server.domain.memo.entity;

import dev.memocode.memo_server.domain.base.entity.AggregateRoot;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor(access = PROTECTED)
@SQLDelete(sql = "UPDATE users SET deleted = true, deleted_at = NOW() WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Table(name = "memo_version")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class MemoVersion extends AggregateRoot {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "memo_id")
    private Memo memo;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "version")
    private Integer version;

    // memoVersion soft delete 적용
    public void delete() {
        this.deleted = true;
        this.deletedAt = Instant.now();
    }
}
