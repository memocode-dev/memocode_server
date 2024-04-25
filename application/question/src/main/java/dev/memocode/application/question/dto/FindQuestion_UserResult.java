package dev.memocode.application.question.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class FindQuestion_UserResult {
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String fullName;
}
