package dev.memocode.domain.memo;

import dev.memocode.domain.user.ImmutableUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImmutableMemo {
    private UUID id;
    private String title;
    private String summary;
    private String content;
    private Boolean visibility;
    private ImmutableUser user;
    private ImmutableMemo formattedMemo;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    private Boolean deleted;
}
