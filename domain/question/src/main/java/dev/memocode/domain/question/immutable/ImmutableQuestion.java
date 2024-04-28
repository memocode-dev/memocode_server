package dev.memocode.domain.question.immutable;

import dev.memocode.domain.user.ImmutableUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImmutableQuestion {
    private UUID id;
    private String title;
    private String content;
    private ImmutableUser user;
    private ImmutableQuestion formattedQuestion;
    @Builder.Default
    private Set<String> tags = new HashSet<>();
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean deleted;
    private Instant deletedAt;
}
