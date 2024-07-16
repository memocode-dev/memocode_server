package dev.memocode.domain.memo;

import dev.memocode.domain.tag.Tag;
import dev.memocode.domain.user.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoCreateDomainDTO {
    @NotNull(message = "VALIDATION_TITLE_NOT_NULL:title은 null일 수 없습니다.")
    @Size(max = 100, message = "VALIDATION_TITLE_SIZE:최대 100자까지 입력가능합니다.")
    private String title;

    @NotNull(message = "VALIDATION_CONTENT_NOT_NULL:content는 null일 수 없습니다.")
    private String content;

    @NotNull(message = "VALIDATION_SUMMARY_NOT_NULL:summary는 null일 수 없습니다.")
    @Size(max = 255, message = "VALIDATION_SUMMARY_SIZE:최대 255자를 입력할 수 있습니다.")
    private String summary;

    @NotNull(message = "VALIDATION_USER_NOT_NULL:user는 null일 수 없습니다.")
    private User user;

    @NotNull(message = "VALIDATION_SECURITY_NOT_NULL:security는 null일 수 없습니다.")
    private Boolean security;

    @NotNull(message = "VALIDATION_TAGS_NOT_NULL:tags는 null일 수 없습니다.")
    private Set<Tag> tags;
}
