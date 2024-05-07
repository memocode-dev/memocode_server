package dev.memocode.application.memo.service;

import dev.memocode.application.core.PageResponse;
import dev.memocode.application.memo.converter.MemoDTOConverter;
import dev.memocode.application.memo.dto.reque.SearchMemoRequest;
import dev.memocode.application.memo.dto.request.*;
import dev.memocode.application.memo.dto.result.*;
import dev.memocode.application.memo.repository.MemoRepository;
import dev.memocode.application.memo.repository.SearchMemoRepository;
import dev.memocode.application.memo.usecase.MemoUseCase;
import dev.memocode.application.tag.InternalTagService;
import dev.memocode.application.user.InternalUserService;
import dev.memocode.domain.memo.ImmutableMemo;
import dev.memocode.domain.memo.Memo;
import dev.memocode.domain.memo.MemoCreateDomainDTO;
import dev.memocode.domain.memo.MemoDomainService;
import dev.memocode.domain.tag.Tag;
import dev.memocode.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemoService implements MemoUseCase {

    private final MemoRepository memoRepository;
    private final SearchMemoRepository searchMemoRepository;
    private final MemoDomainService memoDomainService;
    private final MemoDTOConverter memoDTOConverter;

    private final InternalUserService internalUserService;
    private final InternalMemoService internalMemoService;
    private final InternalTagService internalTagService;

    @Override
    @Transactional
    public UUID createMemo(CreateMemoRequest request) {
        User user = internalUserService.findByIdEnabledUserElseThrow(request.getUserId());
        Set<Tag> tags = request.getTags().stream()
                .map(internalTagService::createTagOrGetTag)
                .collect(Collectors.toSet());

        MemoCreateDomainDTO domainDTO = memoDTOConverter.toDomainDTO(user, request, tags);

        Memo memo = memoDomainService.createMemo(domainDTO);

        memoRepository.save(memo);

        return memo.getId();
    }

    @Override
    @Transactional
    public void updateMemo(UpdateMemoRequest request) {
        Memo memo = internalMemoService.findByIdElseThrow(request.getMemoId());
        User user = internalUserService.findByIdEnabledUserElseThrow(request.getUserId());

        Set<Tag> tags = request.getTags() == null ? null : request.getTags().stream()
                .map(internalTagService::createTagOrGetTag)
                .collect(Collectors.toSet());

        memoDomainService.updateMemo(memo, user, memoDTOConverter.toDomainDTO(request, tags));
    }

    @Override
    @Transactional
    public void deleteMemo(DeleteMemoRequest request) {
        Memo memo = internalMemoService.findByIdElseThrow(request.getMemoId());
        User user = internalUserService.findByIdEnabledUserElseThrow(request.getUserId());

        memoDomainService.softDeleteMemo(memo, user);
    }

    @Override
    public FindMyMemo_MemoResult findMyMemo(FindMyMemoRequest request) {
        Memo memo = internalMemoService.findByIdElseThrow(request.getMemoId());
        User user = internalUserService.findByIdEnabledUserElseThrow(request.getUserId());

        Memo validatedMemo = memoDomainService.findMyMemo(memo, user);

        return memoDTOConverter.toFindMyMemo_MemoResult(validatedMemo);
    }

    // TODO 필요한 필드만 가져올 수 있도록 수정 필요
    @Override
    public List<FindAllMyMemo_MemoResult> findAllMyMemo(FindAllMyMemoRequest request) {
        User user = internalUserService.findByIdEnabledUserElseThrow(request.getUserId());
        List<Memo> memos = internalMemoService.findAllNotDeletedMemo(user);

        List<Memo> validatedMemos = memoDomainService.findAllMyMemo(memos, user);

        return memoDTOConverter.toFindMyMemo_MemoResult(validatedMemos);
    }

    @Override
    public PageResponse<SearchMyMemo_MemoResult> searchMyMemo(SearchMyMemoRequest request) {
        User user = internalUserService.findByIdEnabledUserElseThrow(request.getUserId());
        Page<ImmutableMemo> page =
                searchMemoRepository.searchMyMemo(user, request.getKeyword(), request.getPage(), request.getPageSize());

        List<ImmutableMemo> validatedMemos = memoDomainService.searchMyMemo(page.getContent(), user);

        return PageResponse.<SearchMyMemo_MemoResult>builder()
                .page(page.getNumber())
                .pageSize(page.getSize())
                .totalCount(page.getTotalElements())
                .content(memoDTOConverter.toSearchMyMemo_MemoResult(validatedMemos))
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    @Override
    public FindMemo_MemoResult findMemo(FindMemoRequest request) {
        Memo memo = internalMemoService.findByIdElseThrow(request.getMemoId());

        Memo validatedMemo = memoDomainService.findMemo(memo);

        return memoDTOConverter.toFindMemo_MemoResult(validatedMemo);
    }

    @Override
    public PageResponse<SearchMemo_MemoResult> searchMemo(SearchMemoRequest request) {
        Page<ImmutableMemo> page =
                searchMemoRepository.searchMemo(request.getKeyword(), request.getPage(), request.getPageSize());

        List<ImmutableMemo> validatedMemos = memoDomainService.searchMemo(page.getContent());

        return PageResponse.<SearchMemo_MemoResult>builder()
                .page(page.getNumber())
                .pageSize(page.getSize())
                .totalCount(page.getTotalElements())
                .content(memoDTOConverter.toSearchMemo_MemoResult(validatedMemos))
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}
