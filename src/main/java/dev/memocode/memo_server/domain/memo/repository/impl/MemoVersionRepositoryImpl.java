package dev.memocode.memo_server.domain.memo.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.memocode.memo_server.domain.memo.entity.MemoVersion;
import dev.memocode.memo_server.domain.memo.entity.QMemoVersion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemoVersionRepositoryImpl implements MemoVersionRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    @Override
    public Page<MemoVersion> findAllByMemoVersion(UUID memoId, Pageable pageable) {
        QMemoVersion memoVersion = QMemoVersion.memoVersion;
        BooleanExpression memoIdEq = memoVersion.memo.id.eq(memoId);

        List<MemoVersion> memoVersions = queryFactory
                .selectFrom(memoVersion)
                .where(memoIdEq)
                .orderBy(memoVersion.version.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(memoVersion.count())
                .from(memoVersion)
                .where(memoIdEq)
                .fetchOne();

        return new PageImpl<>(memoVersions, pageable, total);
    }
}
