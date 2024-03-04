package dev.memocode.memo_server.domain.base.external.user.repository;


import dev.memocode.memo_server.domain.base.external.user.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<Author, UUID> {
}
