package dev.memocode.adapter.memo.in.api.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMemoForm {

    @Schema(requiredMode = REQUIRED)
    private String title;

    @Schema(requiredMode = REQUIRED)
    private String content;

    @Schema(requiredMode = REQUIRED)
    private String summary;

    @Schema(requiredMode = REQUIRED)
    @JsonProperty(defaultValue = "false")
    private Boolean security;
}
