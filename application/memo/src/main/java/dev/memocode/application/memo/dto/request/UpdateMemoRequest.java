package dev.memocode.application.memo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemoRequest {
    @NotNull
    private UUID memoId;
    @NotNull
    private UUID userId;
    private String title;
    private String content;
    private String summary;
    private String thumbnailUrl;
    private Boolean visibility;
    private Boolean security;
    private Boolean bookmarked;
    private Set<String> tags;
}
