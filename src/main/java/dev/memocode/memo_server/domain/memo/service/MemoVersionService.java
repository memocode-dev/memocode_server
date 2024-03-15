package dev.memocode.memo_server.domain.memo.service;

import dev.memocode.memo_server.domain.base.exception.GlobalException;
import dev.memocode.memo_server.domain.memo.dto.request.MemoVersionCreateDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoVersionDeleteDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoVersionRequestDetailDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemoVersionDetailDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemoVersionsDTO;
import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memo.entity.MemoVersion;
import dev.memocode.memo_server.domain.memo.repository.MemoVersionRepository;
import dev.memocode.memo_server.usecase.MemoVersionUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static dev.memocode.memo_server.domain.base.exception.GlobalErrorCode.MEMO_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoVersionService implements MemoVersionUseCase {

    private final MemoVersionRepository memoVersionRepository;
    private final InternalMemoService internalMemoService;
    private final InternalMemoVersionService internalMemoVersionService;

    @Transactional
    @Override
    public UUID createMemoVersion(MemoVersionCreateDTO dto) {

        internalMemoService.validMemoOwner(dto.getMemoId(), dto.getAuthorId());

        Memo memo = internalMemoService.findByMemoIdElseThrow(dto.getMemoId());

        Integer lastVersion = internalMemoVersionService.getLastVersion(memo);

        int version = (lastVersion != null) ? lastVersion + 1 : 1;

        MemoVersion memoVersion = MemoVersion.builder()
                .memo(memo)
                .title(memo.getTitle())
                .content(memo.getContent())
                .version(version)
                .build();

        MemoVersion savedMemoVersion = memoVersionRepository.save(memoVersion);

        return savedMemoVersion.getId();
    }

    @Transactional
    @Override
    public void deleteMemoVersion(MemoVersionDeleteDTO dto) {
        internalMemoVersionService.validMemoVersionOwner(dto.getMemoVersionId(), dto.getMemoId(), dto.getAuthorId());

        MemoVersion memoVersion = internalMemoVersionService.findByIdElseThrow(dto.getMemoVersionId());

        memoVersion.delete();
    }

    @Override
    public MemoVersionDetailDTO findMemoVersionDetail(MemoVersionRequestDetailDTO dto) {
        internalMemoVersionService.validMemoVersionOwner(dto.getMemoVersionId(), dto.getMemoId(), dto.getAuthorId());

        MemoVersion memoVersion = internalMemoVersionService.findByIdElseThrow(dto.getMemoVersionId());
        if (memoVersion.getDeleted()){
            throw new GlobalException(MEMO_NOT_FOUND);
        }

        return MemoVersionDetailDTO.of(memoVersion);
    }

    @Override
    public Page<MemoVersionsDTO> findMemoVersions(UUID memoId, UUID authorId, int page, int size) {
        internalMemoService.validMemoOwner(memoId, authorId);

        Page<MemoVersion> pages = memoVersionRepository.findAllByMemoVersion(memoId, PageRequest.of(page, size));

        return MemoVersionsDTO.from(pages);
    }
}
