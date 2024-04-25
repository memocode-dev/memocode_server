package dev.memocode.domain.memo;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemoUpdateDomainDTO {
    @Size(max = 50, message = "VALIDATION_TITLE_SIZE:최대 50자까지 입력가능합니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_\\s~!@#$\t]*$",
            message = "VALIDATION_TITLE_PATTERN:영소문자, 한글 그리고 특수문자(_, 공백, ~, !, @, #, $, 탭)만 입력가능합니다.")
    private String title;

    @Size(max = 255, message = "VALIDATION_SUMMARY_SIZE:최대 255자를 입력할 수 있습니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_\\s~!@#$\t]*$",
            message = "VALIDATION_SUMMARY_PATTERN:영소문자, 한글 그리고 특수문자(_, 공백, ~, !, @, #, $, 탭)만 입력가능합니다.")
    private String summary;

    private String content;

    private Boolean security;
    private Boolean visibility;
    private Boolean bookmarked;
}
