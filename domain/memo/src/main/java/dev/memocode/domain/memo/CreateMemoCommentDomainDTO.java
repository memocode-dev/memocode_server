package dev.memocode.domain.memo;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateMemoCommentDomainDTO {
    @NotNull(message = "VALIDATION_CONTENT_NOT_NULL:content는 null일 수 없습니다.")
    private String content;
}
