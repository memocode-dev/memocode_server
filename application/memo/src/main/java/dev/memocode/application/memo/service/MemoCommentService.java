package dev.memocode.application.memo.service;

import dev.memocode.application.memo.converter.MemoCommentDTOConverter;
import dev.memocode.application.memo.dto.request.CreateChildMemoCommentRequest;
import dev.memocode.application.memo.dto.request.CreateMemoCommentRequest;
import dev.memocode.application.memo.dto.request.DeleteMemoCommentRequest;
import dev.memocode.application.memo.dto.request.UpdateMemoCommentRequest;
import dev.memocode.application.memo.dto.result.FindAllMemoComment_MemoCommentResult;
import dev.memocode.application.memo.repository.MemoCommentRepository;
import dev.memocode.application.memo.usecase.MemoCommentUseCase;
import dev.memocode.application.user.InternalUserService;
import dev.memocode.domain.core.NotFoundException;
import dev.memocode.domain.memo.*;
import dev.memocode.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static dev.memocode.domain.memo.MemoDomainErrorCode.MEMO_COMMENT_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemoCommentService implements MemoCommentUseCase {

    private final InternalMemoService internalMemoService;
    private final InternalUserService internalUserService;

    private final MemoCommentDomainService memoCommentDomainService;
    private final MemoCommentRepository memoCommentRepository;

    private final MemoCommentDTOConverter memoCommentDTOConverter;

    @Override
    @Transactional
    public UUID createMemoComment(CreateMemoCommentRequest request) {
        Memo memo = internalMemoService.findByIdElseThrow(request.getMemoId());
        User user = internalUserService.findByIdEnabledUserElseThrow(request.getUserId());

        CreateMemoCommentDomainDTO dto = CreateMemoCommentDomainDTO.builder()
                .content(request.getContent())
                .build();

        MemoComment memoComment = memoCommentDomainService.createMemoComment(memo, user, dto);
        return memoComment.getId();
    }

    @Override
    @Transactional
    public UUID createChildMemoComment(CreateChildMemoCommentRequest request) {
        Memo memo = internalMemoService.findByIdElseThrow(request.getMemoId());
        MemoComment memoComment = this.findByIdElseThrow(request.getMemoCommentId());
        User user = internalUserService.findByIdEnabledUserElseThrow(request.getUserId());

        CreateMemoCommentDomainDTO dto = CreateMemoCommentDomainDTO.builder()
                .content(request.getContent())
                .build();

        MemoComment childMemoComment =
                memoCommentDomainService.createChildMemoComment(memo, memoComment, user, dto);
        return childMemoComment.getId();
    }

    @Override
    @Transactional
    public void updateMemoComment(UpdateMemoCommentRequest request) {
        Memo memo = internalMemoService.findByIdElseThrow(request.getMemoId());
        MemoComment memoComment = this.findByIdElseThrow(request.getMemoCommentId());
        User user = internalUserService.findByIdEnabledUserElseThrow(request.getUserId());

        UpdateMemoCommentDomainDTO dto = UpdateMemoCommentDomainDTO.builder()
                .content(request.getContent())
                .build();

        memoCommentDomainService.updateMemoComment(memo, memoComment, user, dto);
    }

    @Override
    @Transactional
    public void deleteMemoComment(DeleteMemoCommentRequest request) {
        Memo memo = internalMemoService.findByIdElseThrow(request.getMemoId());
        MemoComment memoComment = this.findByIdElseThrow(request.getMemoCommentId());
        User user = internalUserService.findByIdEnabledUserElseThrow(request.getUserId());

        memoCommentDomainService.deleteMemoComment(memo, memoComment, user);
    }

    @Override
    public List<FindAllMemoComment_MemoCommentResult> findAllMemoComment(UUID memoId) {
        Memo memo = internalMemoService.findByIdElseThrow(memoId);

        List<MemoComment> memoComments = memoCommentDomainService.findAll(memo);
        return memoCommentDTOConverter.toResult(memoComments);
    }

    private MemoComment findByIdElseThrow(UUID memoCommentId) {
        return memoCommentRepository.findById(memoCommentId)
                .orElseThrow(() -> new NotFoundException(MEMO_COMMENT_NOT_FOUND));
    }
}
