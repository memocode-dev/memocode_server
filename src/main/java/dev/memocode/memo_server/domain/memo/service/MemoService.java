package dev.memocode.memo_server.domain.memo.service;

import dev.memocode.memo_server.domain.external.user.entity.Author;
import dev.memocode.memo_server.domain.external.user.service.AuthorService;
import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memo.repository.MemoRepository;
import dev.memocode.memo_server.dto.request.MemoCreateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemoService {

    private final MemoRepository memoRepository;
    private final AuthorService authorService;

    @Transactional
    public Memo createMemo(MemoCreateDTO dto) {

        Author author = authorService.findByAccountIdElseThrow(dto.getAccountId());
        Memo memo = Memo.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .author(author)
                .build();
        return memoRepository.save(memo);
    }
}
