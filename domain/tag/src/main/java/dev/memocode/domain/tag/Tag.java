package dev.memocode.domain.tag;

import dev.memocode.domain.core.BaseEntity;
import dev.memocode.domain.core.SoftDeleteBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor(access = PROTECTED)
@Table(name = "tags")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Tag extends SoftDeleteBaseEntity {

    @Column(name = "name", unique = true)
    @EqualsAndHashCode.Include
    private String name;
}
