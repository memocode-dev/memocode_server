package dev.memocode.domain.tag;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@Validated
public class TagDomainService {

    public Tag createTag(@Valid CreateTagDomainDTO dto) {
        return Tag.builder()
                .id(UUID.randomUUID())
                .name(dto.getName())
                .build();
    }
}
