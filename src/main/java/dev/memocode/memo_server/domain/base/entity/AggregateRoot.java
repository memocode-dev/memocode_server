package dev.memocode.memo_server.domain.base.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

import static lombok.AccessLevel.PROTECTED;

@Getter
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public abstract class AggregateRoot extends BaseEntity {

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean deleted = false;
}
