package dev.memocode.memo_server.domain.memo.entity;

import dev.memocode.memo_server.domain.author.entity.Author;
import dev.memocode.memo_server.domain.base.entity.AggregateRoot;
import dev.memocode.memo_server.domain.base.exception.GlobalException;
import dev.memocode.memo_server.domain.series.entity.Series;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static dev.memocode.memo_server.domain.base.exception.GlobalErrorCode.*;
import static jakarta.persistence.FetchType.LAZY;
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
    private Integer affinity; // 좋아요

    @Column(name = "sequence")
    private Integer sequence;

    @Column(name = "visibility")
    private Boolean visibility;

    @Column(name = "visibility_achieved_at")
    private Instant visibilityAchievedAt;

    @Column(name = "bookmarked")
    private Boolean bookmarked;

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
    public void updateMemo(String title, String content, Boolean visibility, Boolean security) {

        this.title = title == null ? this.title : title;
        this.content = content == null ? this.content : content;

        if (security != null) {
            // 한번 보호 모드가 작동한다면 security를 변경하지 못함
            if (this.security) {
                throw new GlobalException(PROTECT_MEMO_SECURITY_UNMODIFIED);
            }

            // 이미 한번 공개되었다면 보호모드로 변경 못함
            if (visibilityAchievedAt != null) {
                throw new GlobalException(PROTECT_MODE_DISABLED_ONCE_PUBLIC);
            }

            this.security = security;
        }

        if (visibility != null) {
            // 한번 보호 모드가 작동한다면 visibility 필드 사용불가
            if (this.security) {
                throw new GlobalException(PROTECT_MEMO_VISIBILITY_UNMODIFIED);
            }

            this.visibility = visibility;
            this.visibilityAchievedAt = Instant.now();
        }
    }
}
