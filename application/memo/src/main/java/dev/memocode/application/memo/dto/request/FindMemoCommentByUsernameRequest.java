package dev.memocode.application.memo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;

@Data
@Builder
public class FindMemoCommentByUsernameRequest {
    @NotNull
    private String username;
    private Pageable pageable;
}
