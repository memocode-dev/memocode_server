package dev.memocode.domain.user;

import dev.memocode.domain.core.ForbiddenException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Immutable;

import java.time.Instant;
import java.util.UUID;

import static dev.memocode.domain.user.UserDomainErrorCode.USER_NOT_FOUND;
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
public class User {

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

    public String getFullName() {
        return this.firstName + this.lastName;
    }

    public Instant getCreatedAt() {
        return (this.createdAt != null) ? Instant.ofEpochSecond(this.createdAt) : null;
    }

    /**
     * 유저가 활성화되어 있는지 확인하는 메서드
     *  - 활성화되어 있다면 통과, 활성화되어있지 않다면 exception 발생
     */
    public void assertIsEnabled() {
        if (!this.enabled) {
            throw new ForbiddenException(USER_NOT_FOUND);
        }
    }
}
