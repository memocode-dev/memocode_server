package dev.memocode.adapter.adapter_batch_core;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class MeilisearchUser {
    private UUID id;
    private String username;
    private Boolean enabled;
}
