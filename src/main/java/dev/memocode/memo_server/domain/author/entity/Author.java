package dev.memocode.memo_server.domain.author.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Immutable;

import java.time.Instant;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Immutable
@Entity
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Table(name = "user_entity")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Author {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private UUID id;

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "created_timestamp")
    private Long createdAt;

    public String getNickname() {
        return this.firstName + this.lastName;
    }

    public Instant getCreatedAt() {
        return (this.createdAt != null) ? Instant.ofEpochSecond(this.createdAt) : null;
    }
}
