package dev.memocode.memo_server.domain.memo.repository.impl;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memo.repository.MemoRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static dev.memocode.memo_server.domain.memo.entity.QMemo.memo;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemoRepositoryImpl implements MemoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Memo> findByAuthorId(UUID authorId) {
        return queryFactory
                .selectFrom(memo)
                .where(memo.author.id.eq(authorId))
                .orderBy(memo.sequence.asc())
                .fetch();
    }

    @Override
    public Page<Memo> findByPosts(Pageable pageable) {

        List<Memo> posts = queryFactory
                .selectFrom(memo)
                .where(memo.visibility.eq(true))
                .orderBy(memo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(memo.count())
                .from(memo)
                .where(memo.visibility.eq(true));

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchOne);
    }

    @Override
    public Integer getLastSequence(UUID authorId, int defaultValue) {
        JPAQuery<Memo> query = queryFactory
                .select(memo)
                .from(memo)
                .where(memo.author.id.eq(authorId))
                .orderBy(memo.sequence.desc())
                .limit(1);

        Memo memo = query.fetchOne();

        return memo == null ? defaultValue : memo.getSequence();
    }
}
