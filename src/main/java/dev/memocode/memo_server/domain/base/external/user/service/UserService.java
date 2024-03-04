package dev.memocode.memo_server.domain.base.external.user.service;


import dev.memocode.memo_server.domain.base.external.user.entity.Author;
import dev.memocode.memo_server.domain.base.external.user.repository.UserRepository;
import dev.memocode.memo_server.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Author findByIdElseThrow(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(null));
    }
}
