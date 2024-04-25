package dev.memocode.application.memo.dto.result;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class FindAllMemoComment_UserResult {
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String fullName;
}
