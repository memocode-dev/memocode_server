package dev.memocode.memo_server.domain.base.memo;

import dev.memocode.memo_server.domain.base.entity.AggregateRoot;
import dev.memocode.memo_server.domain.base.external.user.entity.Author;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor(access = PROTECTED)
@Table(name = "memos")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Memo extends AggregateRoot {

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "authors_id")
    private Author author;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "selected_memo_version_id")
    private MemoVersion selectedMemoVersion;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "series_id")
    private Series series;

    @Column(name = "affinity")
    private Long affinity; // 좋아요

    private Integer orderBy;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_memo_id")
    private Memo parentMemo;

    @OneToMany(mappedBy = "parentMemo")
    private Set<Memo> childMemos = new HashSet<>();
}
