package dev.memocode.memo_server.domain.memo.repository.impl;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.memocode.memo_server.domain.memo.entity.MemoVersion;
import dev.memocode.memo_server.domain.memo.entity.QMemoVersion;
import dev.memocode.memo_server.domain.memo.repository.MemoVersionRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static dev.memocode.memo_server.domain.memo.entity.QMemoVersion.memoVersion;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemoVersionRepositoryImpl implements MemoVersionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemoVersion> findAllByMemoVersion(UUID memoId) {

        return queryFactory
                .selectFrom(QMemoVersion.memoVersion)
                .where(memoVersion.memo.id.eq(memoId))
                .orderBy(memoVersion.version.desc())
                .fetch();
    }
}
