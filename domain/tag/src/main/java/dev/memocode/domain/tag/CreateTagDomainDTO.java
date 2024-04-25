package dev.memocode.domain.tag;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateTagDomainDTO {
    private String name;
}
