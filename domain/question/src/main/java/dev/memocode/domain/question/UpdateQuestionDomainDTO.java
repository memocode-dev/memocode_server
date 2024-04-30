package dev.memocode.domain.question;

import dev.memocode.domain.tag.Tag;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UpdateQuestionDomainDTO {
    @Size(max = 50, message = "VALIDATION_TITLE_SIZE:최대 50자까지 입력가능합니다.")
    private String title;

    private String content;

    private Set<Tag> tags;
}
