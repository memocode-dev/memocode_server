package dev.memocode.memo_server.domain.memo.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memo.entity.QMemo;
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
public class MemoRepositoryImpl implements MemoRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Memo> findByAuthorId(UUID authorId) {
        QMemo memo = QMemo.memo;
        BooleanExpression authorIdEq = memo.author.id.eq(authorId);

        return queryFactory
                .selectFrom(memo)
                .where(authorIdEq)
                .orderBy(memo.sequence.asc())
                .fetch();
    }
}
