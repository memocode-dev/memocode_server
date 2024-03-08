package dev.memocode.memo_server.domain.external.author.entity;

import dev.memocode.memo_server.domain.base.entity.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Immutable
@Entity
@SuperBuilder
@NoArgsConstructor(access = PROTECTED)
@Table(name = "users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Author extends AggregateRoot {

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "account_id", unique = true)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID accountId;
}
