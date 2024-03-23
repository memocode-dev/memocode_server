package dev.memocode.memo_server.domain.memocomment.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.memocode.memo_server.domain.memocomment.entity.Comment;
import dev.memocode.memo_server.domain.memocomment.entity.QComment;
import dev.memocode.memo_server.domain.memocomment.repository.CommentRepositoryCustom;
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
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    @Override
    public Page<Comment> findAllByMemoId(UUID memoId, Pageable pageable) {
        QComment comment = QComment.comment;

        List<Comment> comments = queryFactory
                .selectFrom(comment)
                .where(comment.memo.id.eq(memoId), comment.parentComment.isNull())
                .orderBy(comment.createdAt.asc(), comment.content.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.memo.id.eq(memoId), comment.parentComment.isNull())
                .fetchOne();

        log.info("comment total = {}", total);

        return new PageImpl<>(comments, pageable, total);
    }
}
