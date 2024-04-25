package dev.memocode.application.tag;

import dev.memocode.domain.tag.CreateTagDomainDTO;
import dev.memocode.domain.tag.Tag;
import dev.memocode.domain.tag.TagDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class InternalTagService {

    private final TagRepository tagRepository;

    private final TagDomainService tagDomainService;

    public Tag createTagOrGetTag(String name) {
        Optional<Tag> _tag = tagRepository.findByName(name);

        if (_tag.isPresent()) {
            return _tag.get();
        }

        CreateTagDomainDTO dto = CreateTagDomainDTO.builder()
                .name(name)
                .build();

        Tag tag = tagDomainService.createTag(dto);

        return tagRepository.save(tag);
    }
}
