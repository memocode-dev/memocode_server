package dev.memocode.domain.memo;

import dev.memocode.domain.core.BusinessRuleViolationException;
import dev.memocode.domain.core.ErrorDetail;
import dev.memocode.domain.user.User;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static dev.memocode.domain.memo.MemoDomainErrorCode.PARENT_MEMO_COMMENT_HAS_PARENT;

@Service
@Validated
public class MemoCommentDomainService {
    public MemoComment createMemoComment(Memo memo, User user, @Valid CreateMemoCommentDomainDTO dto) {
        memo.assertIsNotDeleted();
        memo.assertIsVisibility();
        return memo.addComment(dto.getContent(), user);
    }

    public void updateMemoComment(
            Memo memo, MemoComment memoComment, User user, @Valid UpdateMemoCommentDomainDTO dto) {
        memo.assertIsNotDeleted();
        memo.assertIsVisibility();
        memoComment.assertIsNotDeleted();
        memoComment.assertIsMemoCommentOwner(user);
        memo.updateComment(memoComment, dto.getContent());
    }

    public void deleteMemoComment(Memo memo, MemoComment memoComment, User user) {
        memo.assertIsNotDeleted();
        memo.assertIsVisibility();
        memoComment.assertIsNotDeleted();
        memoComment.assertIsMemoCommentOwner(user);
        memo.removeComment(memoComment);
    }

    public List<MemoComment> findAllByParentMemoCommentIsNull(Memo memo) {
        memo.assertIsNotDeleted();
        memo.assertIsVisibility();
        return memo.getMemoComments();
    }

    public MemoComment createChildMemoComment(
            Memo memo, MemoComment parentMemoComment, User user, @Valid CreateMemoCommentDomainDTO dto) {
        memo.assertIsNotDeleted();
        memo.assertIsVisibility();
        parentMemoComment.assertIsNotDeleted();

        if (parentMemoComment.getParentMemoComment() != null) {
            throw new BusinessRuleViolationException(PARENT_MEMO_COMMENT_HAS_PARENT);
        }

        return memo.addChildComment(parentMemoComment, user, dto.getContent());
    }

    public List<MemoComment> findAll(List<MemoComment> comments) {
        return comments.stream()
                .filter(memoComments -> !memoComments.getDeleted())
                .toList();
    }
}
