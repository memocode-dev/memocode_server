package dev.memocode.domain.core;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter
@SuperBuilder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public abstract class SoftDeleteBaseEntity extends BaseEntity {

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "is_deleted")
    private Boolean deleted;

    /**
     * 데이터를 삭제합니다.
     */
    protected void delete() {
        this.deleted = true;
        this.deletedAt = Instant.now();
    }

    /**
     * 데이터를 복구합니다.
     */
    protected void recover() {
        this.deleted = false;
        this.deletedAt = null;
    }
}
