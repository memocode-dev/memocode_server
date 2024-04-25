package dev.memocode.domain.memo;

import dev.memocode.domain.user.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoCreateDomainDTO {
    @NotNull(message = "VALIDATION_TITLE_NOT_NULL:title은 nulll일 수 없습니다.")
    @Size(max = 50, message = "VALIDATION_TITLE_SIZE:최대 50자까지 입력가능합니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_\\s~!@#$\t]*$",
            message = "VALIDATION_TITLE_PATTERN:영소문자, 한글 그리고 특수문자(_, 공백, ~, !, @, #, $, 탭)만 입력가능합니다.")
    private String title;

    @NotNull(message = "VALIDATION_CONTENT_NOT_NULL:content는 null일 수 없습니다.")
    private String content;

    @NotNull(message = "VALIDATION_SUMMARY_NOT_NULL:summary는 nulll일 수 없습니다.")
    @Size(max = 255, message = "VALIDATION_SUMMARY_SIZE:최대 255자를 입력할 수 있습니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_\\s~!@#$\t]*$",
            message = "VALIDATION_SUMMARY_PATTERN:영소문자, 한글 그리고 특수문자(_, 공백, ~, !, @, #, $, 탭)만 입력가능합니다.")
    private String summary;

    @NotNull(message = "VALIDATION_USER_NOT_NULL:user는 null일 수 없습니다.")
    private User user;

    @NotNull(message = "VALIDATION_SECURITY_NOT_NULL:security는 null일 수 없습니다.")
    private Boolean security;
}
