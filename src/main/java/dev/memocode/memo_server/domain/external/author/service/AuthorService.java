package dev.memocode.memo_server.domain.external.author.service;


import dev.memocode.memo_server.domain.external.author.entity.Author;
import dev.memocode.memo_server.domain.external.author.repository.AuthorRepository;
import dev.memocode.memo_server.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static dev.memocode.memo_server.exception.GlobalErrorCode.AUTHOR_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public Author findByAccountIdElseThrow(UUID accountId) {
        return findByAccountId(accountId)
                .orElseThrow(() -> new GlobalException(AUTHOR_NOT_FOUND));
    }

    public Optional<Author> findByAccountId(UUID accountId) {
        return authorRepository.findByAccountId(accountId);
    }
}
