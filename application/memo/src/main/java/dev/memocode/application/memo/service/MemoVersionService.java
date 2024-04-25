package dev.memocode.application.memo.service;

import dev.memocode.application.memo.converter.MemoVersionDTOConverter;
import dev.memocode.application.memo.dto.request.CreateMemoVersionRequest;
import dev.memocode.application.memo.dto.request.DeleteMemoVersionRequest;
import dev.memocode.application.memo.dto.request.FindAllMyMemoVersionRequest;
import dev.memocode.application.memo.dto.request.FindMyMemoVersionRequest;
import dev.memocode.application.memo.dto.result.FindAllMyMemoVersion_MemoVersionResult;
import dev.memocode.application.memo.dto.result.FindMyMemoVersion_MemoVersionResult;
import dev.memocode.application.memo.usecase.MemoVersionUseCase;
import dev.memocode.application.user.InternalUserService;
import dev.memocode.domain.memo.Memo;
import dev.memocode.domain.memo.MemoVersion;
import dev.memocode.domain.memo.MemoVersionDomainService;
import dev.memocode.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemoVersionService implements MemoVersionUseCase {

    private final InternalMemoService internalMemoService;
    private final InternalUserService internalUserService;

    private final MemoVersionDomainService memoVersionDomainService;

    private final MemoVersionDTOConverter memoVersionDTOConverter;

    @Override
    @Transactional
    public UUID createMemoVersion(CreateMemoVersionRequest request) {
        Memo memo = internalMemoService.findByIdElseThrow(request.getMemoId());
        User user = internalUserService.findByIdEnabledUserElseThrow(request.getUserId());

        MemoVersion memoVersion = memoVersionDomainService.createMemoVersion(memo, user);

        return memoVersion.getId();
    }

    @Override
    @Transactional
    public void deleteMemoVersion(DeleteMemoVersionRequest request) {
        Memo memo = internalMemoService.findByIdElseThrow(request.getMemoId());
        User user = internalUserService.findByIdEnabledUserElseThrow(request.getUserId());

        memoVersionDomainService.removeMemoVersion(memo, user, request.getMemoVersionId());
    }

    @Override
    public FindMyMemoVersion_MemoVersionResult findMyMemoVersion(FindMyMemoVersionRequest request) {
        Memo memo = internalMemoService.findByIdElseThrow(request.getMemoId());
        User user = internalUserService.findByIdEnabledUserElseThrow(request.getUserId());

        MemoVersion memoVersion = memoVersionDomainService.findMyMemoVersion(memo, user, request.getMemoVersionId());
        return memoVersionDTOConverter.toResponse(memoVersion);
    }

    @Override
    public List<FindAllMyMemoVersion_MemoVersionResult> findAllMyMemoVersion(FindAllMyMemoVersionRequest request) {
        Memo memo = internalMemoService.findByIdElseThrow(request.getMemoId());
        User user = internalUserService.findByIdEnabledUserElseThrow(request.getUserId());

        List<MemoVersion> memoVersions = memoVersionDomainService.findAllMyMemoVersion(memo, user);

        return memoVersionDTOConverter.toResponse(memoVersions);
    }
}
