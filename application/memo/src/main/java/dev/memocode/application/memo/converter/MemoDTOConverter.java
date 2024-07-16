package dev.memocode.application.memo.converter;

import dev.memocode.application.memo.dto.request.CreateMemoRequest;
import dev.memocode.application.memo.dto.request.UpdateMemoRequest;
import dev.memocode.application.memo.dto.result.*;
import dev.memocode.domain.memo.ImmutableMemo;
import dev.memocode.domain.memo.Memo;
import dev.memocode.domain.memo.MemoCreateDomainDTO;
import dev.memocode.domain.memo.MemoUpdateDomainDTO;
import dev.memocode.domain.tag.Tag;
import dev.memocode.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MemoDTOConverter {

    public MemoCreateDomainDTO toDomainDTO(User user, CreateMemoRequest dto, Set<Tag> tags) {
        return MemoCreateDomainDTO.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .summary(dto.getSummary())
                .user(user)
                .security(dto.getSecurity())
                .tags(tags)
                .build();
    }

    public MemoUpdateDomainDTO toDomainDTO(UpdateMemoRequest dto, Set<Tag> tags) {
        return MemoUpdateDomainDTO.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .summary(dto.getSummary())
                .thumbnailUrl(dto.getThumbnailUrl())
                .security(dto.getSecurity())
                .visibility(dto.getVisibility())
                .bookmarked(dto.getBookmarked())
                .tags(tags)
                .build();
    }

    public FindMyMemo_MemoResult toFindMyMemo_MemoResult(Memo memo) {
        return FindMyMemo_MemoResult.builder()
                .id(memo.getId())
                .title(memo.getTitle())
                .content(memo.getContent())
                .summary(memo.getSummary())
                .thumbnailUrl(memo.getThumbnailUrl())
                .author(toFindMemo_AuthorResult(memo.getUser()))
                .visibility(memo.getVisibility())
                .bookmarked(memo.getBookmarked())
                .security(memo.getSecurity())
                .createdAt(memo.getCreatedAt())
                .updatedAt(memo.getUpdatedAt())
                .tags(memo.getMemoTags().stream()
                        .map(memoTag -> memoTag.getTag().getName())
                        .collect(Collectors.toSet()))
                .build();
    }

    public FindMyMemo_UserResult toFindMemo_AuthorResult(User user) {
        return FindMyMemo_UserResult.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public List<FindAllMyMemo_MemoResult> toFindMyMemo_MemoResult(List<Memo> memos) {
        return memos.stream().map(this::toFindAllMemo_MemoResult)
                .toList();
    }

    private FindAllMyMemo_MemoResult toFindAllMemo_MemoResult(Memo memo) {
        return FindAllMyMemo_MemoResult.builder()
                .id(memo.getId())
                .title(memo.getTitle())
                .thumbnailUrl(memo.getThumbnailUrl())
                .visibility(memo.getVisibility())
                .bookmarked(memo.getBookmarked())
                .security(memo.getSecurity())
                .tags(memo.getMemoTags().stream()
                        .map(memoTag -> memoTag.getTag().getName())
                        .collect(Collectors.toSet()))
                .createdAt(memo.getCreatedAt())
                .updatedAt(memo.getUpdatedAt())
                .build();
    }

    public List<SearchMyMemo_MemoResult> toSearchMyMemo_MemoResult(List<ImmutableMemo> memos) {
        return  memos.stream()
                .map(this::toSearchMyMemo_MemoResult)
                .toList();
    }

    public SearchMyMemo_MemoResult toSearchMyMemo_MemoResult(ImmutableMemo memo) {
        return SearchMyMemo_MemoResult.builder()
                .id(memo.getId())
                .title(memo.getTitle())
                .summary(memo.getSummary())
                .formattedMemo(toSearchMyMemo_FormattedMemoResult(memo.getFormattedMemo()))
                .createdAt(memo.getCreatedAt())
                .updatedAt(memo.getUpdatedAt())
                .tags(memo.getTags())
                .build();
    }

    public SearchMyMemo_FormattedMemoResult toSearchMyMemo_FormattedMemoResult(ImmutableMemo formattedMemo) {
        return SearchMyMemo_FormattedMemoResult.builder()
                .id(formattedMemo.getId())
                .title(formattedMemo.getTitle())
                .summary(formattedMemo.getSummary())
                .content(formattedMemo.getContent())
                .createdAt(formattedMemo.getCreatedAt())
                .updatedAt(formattedMemo.getUpdatedAt())
                .build();
    }

    public List<SearchMemo_MemoResult> toSearchMemo_MemoResult(List<ImmutableMemo> memos) {
        return memos.stream().map(this::toSearchMemo_MemoResult)
                .toList();
    }

    public SearchMemo_MemoResult toSearchMemo_MemoResult(ImmutableMemo memo) {
        return SearchMemo_MemoResult.builder()
                .id(memo.getId())
                .title(memo.getTitle())
                .user(SearchMemo_UserResult.builder()
                        .id(memo.getUser().getId())
                        .username(memo.getUser().getUsername())
                        .build())
                .formattedMemo(this.toSearchMemo_FormattedMemoResult(memo.getFormattedMemo()))
                .summary(memo.getSummary())
                .tags(memo.getTags())
                .createdAt(memo.getCreatedAt())
                .updatedAt(memo.getUpdatedAt())
                .build();
    }

    public SearchMemo_FormattedMemoResult toSearchMemo_FormattedMemoResult(ImmutableMemo formattedMemo) {

        SearchMemo_UserResult user = SearchMemo_UserResult.builder()
                .id(formattedMemo.getUser().getId())
                .username(formattedMemo.getUser().getUsername())
                .build();

        return SearchMemo_FormattedMemoResult.builder()
                .id(formattedMemo.getId())
                .title(formattedMemo.getTitle())
                .content(formattedMemo.getContent())
                .summary(formattedMemo.getSummary())
                .user(user)
                .createdAt(formattedMemo.getCreatedAt())
                .updatedAt(formattedMemo.getUpdatedAt())
                .build();
    }



    public FindMemo_MemoResult toFindMemo_MemoResult(Memo validatedMemo) {
        FindMemo_UserResult findMemoUserResult = toFindMemo_UserResult(validatedMemo.getUser());

        return FindMemo_MemoResult.builder()
                .id(validatedMemo.getId())
                .title(validatedMemo.getTitle())
                .summary(validatedMemo.getSummary())
                .content(validatedMemo.getContent())
                .thumbnailUrl(validatedMemo.getThumbnailUrl())
                .user(findMemoUserResult)
                .createdAt(validatedMemo.getCreatedAt())
                .updatedAt(validatedMemo.getUpdatedAt())
                .tags(validatedMemo.getMemoTags().stream()
                        .map(memoTag -> memoTag.getTag().getName())
                        .collect(Collectors.toSet())
                )
                .build();
    }

    public FindMemo_UserResult toFindMemo_UserResult(User user) {
        return FindMemo_UserResult.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
