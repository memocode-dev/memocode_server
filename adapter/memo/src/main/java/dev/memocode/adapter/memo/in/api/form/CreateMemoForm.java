package dev.memocode.adapter.memo.in.api.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
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

    @Schema(requiredMode = NOT_REQUIRED)
    @JsonProperty(defaultValue = "false")
    private Boolean security;

    @Schema(requiredMode = REQUIRED)
    private Set<String> tags;
}
