package dev.memocode.domain.memo;

import dev.memocode.domain.tag.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
public class MemoUpdateDomainDTO {
    @Size(max = 50, message = "VALIDATION_TITLE_SIZE:최대 50자까지 입력가능합니다.")
    private String title;

    @Size(max = 255, message = "VALIDATION_SUMMARY_SIZE:최대 255자를 입력할 수 있습니다.")
    private String summary;

    private String content;

    private Boolean security;
    private Boolean visibility;
    private Boolean bookmarked;

    private Set<Tag> tags;
}
