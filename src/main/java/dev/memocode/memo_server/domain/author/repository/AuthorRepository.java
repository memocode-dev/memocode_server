package dev.memocode.memo_server.domain.author.repository;


import dev.memocode.memo_server.domain.author.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID> {
}
