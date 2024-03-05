package dev.memocode.memo_server.domain.memo.entity;

import dev.memocode.memo_server.domain.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor(access = PROTECTED)
@Table(name = "selected_memo_version")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class SelectedMemoVersion extends BaseEntity {

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "memo_id")
    private Memo memo;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "memo_version_id")
    private MemoVersion memoVersion;
}
