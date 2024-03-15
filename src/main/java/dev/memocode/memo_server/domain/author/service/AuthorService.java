package dev.memocode.memo_server.domain.author.service;


import dev.memocode.memo_server.domain.author.entity.Author;
import dev.memocode.memo_server.domain.author.repository.AuthorRepository;
import dev.memocode.memo_server.domain.base.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static dev.memocode.memo_server.domain.base.exception.GlobalErrorCode.AUTHOR_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public Author findByIdElseThrow(UUID userId) {
        return findById(userId)
                .orElseThrow(() -> new GlobalException(AUTHOR_NOT_FOUND));
    }

    public Optional<Author> findById(UUID userId) {
        return authorRepository.findById(userId);
    }
}
