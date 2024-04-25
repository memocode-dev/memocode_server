package dev.memocode.application.memo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UpdateMemoRequest {
    @NotNull
    private UUID memoId;
    @NotNull
    private UUID authorId;
    private String title;
    private String content;
    private String summary;
    private Boolean visibility;
    private Boolean security;
    private Boolean bookmarked;
}
