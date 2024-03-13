package dev.memocode.memo_server.domain.memo.entity;

import dev.memocode.memo_server.domain.base.entity.AggregateRoot;
import dev.memocode.memo_server.domain.external.author.entity.Author;
import dev.memocode.memo_server.domain.series.entity.Series;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor(access = PROTECTED)
@Table(name = "memos")
@SQLDelete(sql = "UPDATE users SET deleted = true, deleted_at = NOW() WHERE id = ?")
@SQLRestriction("is_deleted = false")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Memo extends AggregateRoot {

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "series_id")
    private Series series;

    @Column(name = "affinity")
    @Builder.Default
    private Integer affinity = 0; // 좋아요

    @Column(name = "sequence")
    private Integer sequence;

    @Column(name = "visibility")
    private Boolean visibility;

    @Column(name = "security")
    private Boolean security;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_memo_id")
    private Memo parentMemo;

    @OneToMany(mappedBy = "parentMemo")
    @Builder.Default
    private Set<Memo> childMemos = new HashSet<>();

    // 메모 삭제 (soft delete)
    public void delete() {
        this.deleted = true;
        this.deletedAt = Instant.now();
    }

    // 메모 수정
    public void updateMemo(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void updateVisibility(Boolean visibility) {
        this.visibility = visibility;
    }
}
