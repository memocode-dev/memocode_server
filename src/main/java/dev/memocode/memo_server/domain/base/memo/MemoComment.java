package dev.memocode.memo_server.domain.base.memo;

import dev.memocode.memo_server.domain.base.entity.AggregateRoot;
import dev.memocode.memo_server.domain.base.external.user.entity.Author;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor(access = PROTECTED)
@Table(name = "memo_comments")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class MemoComment extends AggregateRoot {

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = LAZY)
    private Author author;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_post_comment_id")
    private MemoComment parentMemoComment;

    @OneToMany(mappedBy = "parentMemoComment")
    private List<MemoComment> childMemoComments = new ArrayList<>();
}
