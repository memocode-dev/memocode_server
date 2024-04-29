package dev.memocode.application.question.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SearchQuestion_UserResult {
    private UUID id;
    private String username;
}
