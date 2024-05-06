package dev.memocode.domain.question;

import dev.memocode.domain.tag.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class CreateQuestionDomainDTO {
    @NotNull(message = "VALIDATION_TITLE_NOT_NULL:title은 null일 수 없습니다.")
    @Size(max = 50, message = "VALIDATION_TITLE_SIZE:최대 50자까지 입력가능합니다.")
    private String title;

    @NotNull(message = "VALIDATION_CONTENT_NOT_NULL:content은 null일 수 없습니다.")
    private String content;

    @NotNull(message = "VALIDATION_TAGS_NOT_NULL:tags은 null일 수 없습니다.")
    private Set<Tag> tags;
}
