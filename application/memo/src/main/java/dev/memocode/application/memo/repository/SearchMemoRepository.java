package dev.memocode.application.memo.repository;

import dev.memocode.domain.memo.Memo;
import dev.memocode.domain.user.User;
import org.springframework.data.domain.Page;

public interface SearchMemoRepository {
    Page<Memo> searchMyMemo(User user, String keyword, int page, int pageSize);
    Page<Memo> searchMemo(String keyword, int page, int pageSize);
}
