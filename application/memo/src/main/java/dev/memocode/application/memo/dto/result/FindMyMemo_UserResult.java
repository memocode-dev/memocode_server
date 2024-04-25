package dev.memocode.application.memo.dto.result;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class FindMyMemo_UserResult {
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String fullName;
    private Instant createdAt;
}
