package dev.memocode.application.memo.repository;

import dev.memocode.domain.memo.ImmutableMemo;
import dev.memocode.domain.user.User;
import org.springframework.data.domain.Page;

public interface SearchMemoRepository {
    Page<ImmutableMemo> searchMyMemo(User user, String keyword, int page, int pageSize);
    Page<ImmutableMemo> searchMemoByKeyword(String keyword, int page, int pageSize);
    Page<ImmutableMemo> searchMemoByUsername(String username, int page, int pageSize);
}
