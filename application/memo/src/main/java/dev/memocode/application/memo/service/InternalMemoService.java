package dev.memocode.application.memo.service;

import dev.memocode.application.memo.repository.MemoRepository;
import dev.memocode.domain.core.NotFoundException;
import dev.memocode.domain.memo.Memo;
import dev.memocode.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static dev.memocode.domain.memo.MemoDomainErrorCode.MEMO_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class InternalMemoService {

    private final MemoRepository memoRepository;

    public Memo findByIdElseThrow(UUID memoId) {
        return memoRepository.findById(memoId)
                .orElseThrow(() -> new NotFoundException(MEMO_NOT_FOUND));
    }

    public List<Memo> findAllNotDeletedMemo(User user) {
        return memoRepository.findAllByUserAndDeletedAtIsNullOrderByCreatedAt(user);
    }
}
