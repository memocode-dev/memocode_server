package dev.memocode.adapter.adapter_batch_memo.out.converter;

import dev.memocode.adapter.adapter_batch_core.MeilisearchUser;
import dev.memocode.adapter.adapter_batch_memo.out.dto.MeilisearchMemo;
import dev.memocode.domain.memo.Memo;
import dev.memocode.domain.memo.MemoTag;
import dev.memocode.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MeilisearchMemoConverter {

    public MeilisearchMemo toMeilisearchMemo(Memo memo) {
        User user = memo.getUser();
        MeilisearchUser meilisearchUser = MeilisearchUser.builder()
                .id(user.getId())
                .username(user.getUsername())
                .enabled(user.getEnabled())
                .build();

        Set<String> tags = memo.getMemoTags().stream()
                .map(memoTag -> memoTag.getTag().getName())
                .collect(Collectors.toSet());

        return MeilisearchMemo.builder()
                .id(memo.getId())
                .title(memo.getTitle())
                .content(memo.getContent())
                .summary(memo.getSummary())
                .userId(user.getId())
                .user(meilisearchUser)
                .visibility(memo.getVisibility())
                .bookmarked(memo.getBookmarked())
                .security(memo.getSecurity())
                .createdAt(memo.getCreatedAt())
                .updatedAt(memo.getUpdatedAt())
                .deletedAt(memo.getDeletedAt())
                .deleted(memo.getDeleted())
                .tags(tags)
                .build();
    }
}
