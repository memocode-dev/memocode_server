package dev.memocode.memo_server.domain.memo.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memo.entity.MemoVersion;
import dev.memocode.memo_server.domain.memo.entity.QMemo;
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

    @Override
    public Page<Memo> findByPost(Pageable pageable) {
        QMemo post = QMemo.memo;

        List<Memo> posts = queryFactory
                .selectFrom(post)
                .where(post.visibility.eq(true))
                .orderBy(post.sequence.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(post.count())
                .from(post)
                .where(post.visibility.eq(true))
                .fetchOne();

        log.info("total = {}", total);

        return new PageImpl<>(posts, pageable, total);
    }
}
