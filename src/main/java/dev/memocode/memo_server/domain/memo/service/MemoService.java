package dev.memocode.memo_server.domain.memo.service;

import dev.memocode.memo_server.domain.author.entity.Author;
import dev.memocode.memo_server.domain.author.service.AuthorService;
import dev.memocode.memo_server.domain.base.exception.GlobalException;
import dev.memocode.memo_server.domain.memo.dto.request.MemoCreateDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoDeleteDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoUpdateDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemoDetailDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemosDTO;
import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memo.mapper.MemoMapper;
import dev.memocode.memo_server.domain.memo.repository.MemoRepository;
import dev.memocode.memo_server.usecase.MemoUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static dev.memocode.memo_server.domain.base.exception.GlobalErrorCode.MEMO_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemoService implements MemoUseCase {

    private final MemoRepository memoRepository;

    private final AuthorService authorService;

    private final MemoMapper memoMapper;

    private final InternalMemoService internalMemoService;
    private final static int DEFAULT_ADD_INDEX = 1024;

    /**
     * 메모 생성
     */
    @Transactional
    @Override
    public UUID createMemo(MemoCreateDTO dto) {
        Author author = authorService.findByIdElseThrow(dto.getAuthorId());

        Integer lastSequence = getLastSequence(author);

        Memo memo = Memo.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .author(author)
                .affinity(0)
                .sequence(lastSequence + DEFAULT_ADD_INDEX)
                .visibility(false)
                .security(false)
                .build();

        Memo savedMemo = memoRepository.save(memo);

        return savedMemo.getId();
    }

    private Integer getLastSequence(Author author) {
        return internalMemoService.getLastSequence(author.getId());
    }

    @Transactional
    @Override
    public void deleteMemo(MemoDeleteDTO dto) {
        internalMemoService.validMemoOwner(dto.getMemoId(), dto.getAuthorId());

        Memo memo = internalMemoService.findByMemoIdElseThrow(dto.getMemoId());

        // soft delete 적용
        memo.delete();
    }

    @Transactional
    @Override
    public void updateMemo(MemoUpdateDTO dto) {
        internalMemoService.validMemoOwner(dto.getMemoId(), dto.getAuthorId());

        Memo memo = internalMemoService.findByMemoIdElseThrow(dto.getMemoId());

        memo.updateMemo(dto.getTitle(), dto.getContent(), dto.getVisibility(), dto.getSecurity());
    }

    @Override
    public MemoDetailDTO findMemo(UUID memoId, UUID authorId) {
        internalMemoService.validMemoOwner(memoId, authorId);

        Memo memo = internalMemoService.findByMemoIdElseThrow(memoId);

        if (memo.getDeleted()){
            throw new GlobalException(MEMO_NOT_FOUND);
        }

        return memoMapper.entity_to_memoDetailDTO(memo);
    }

    @Override
    public MemosDTO findMemos(UUID authorId) {
        List<Memo> memos = memoRepository.findByAuthorId(authorId);

        return memoMapper.entity_to_memosDTO(memos);
    }
}
