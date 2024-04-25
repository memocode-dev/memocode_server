package dev.memocode.application.memo.converter;

import dev.memocode.application.memo.dto.result.FindAllMemoComment_MemoCommentResult;
import dev.memocode.application.memo.dto.result.FindAllMemoComment_UserResult;
import dev.memocode.domain.memo.MemoComment;
import dev.memocode.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemoCommentDTOConverter {

    // TODO 비즈니스에 중요한 부분이라 이부분을 도메인 계층으로 옮겨야할 것 같음
    private final static String DELETED_MEMO_COMMENT_CONTENT = "삭제된 댓글입니다.";

    public List<FindAllMemoComment_MemoCommentResult> toResult(List<MemoComment> memoComments) {
        return memoComments.stream().map(this::toFindAllMemoComment_MemoCommentResult)
                .toList();
    }

    public FindAllMemoComment_UserResult toFindAllMemoComment_UserResult(User user) {
        return FindAllMemoComment_UserResult.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .build();
    }

    public FindAllMemoComment_MemoCommentResult toFindAllMemoComment_MemoCommentResult(MemoComment memoComment) {
        List<FindAllMemoComment_MemoCommentResult> childComments = memoComment.getChildMemoComments().stream()
                .map(this::toFindAllMemoComment_MemoCommentResult)
                .toList();

        return FindAllMemoComment_MemoCommentResult.builder()
                .id(memoComment.getId())
                // TODO 비즈니스에 중요한 부분이라 이부분을 도메인 계층으로 옮겨야할 것 같음
                .content(memoComment.isDeleted() ? DELETED_MEMO_COMMENT_CONTENT : memoComment.getContent())
                .user(this.toFindAllMemoComment_UserResult(memoComment.getUser()))
                // TODO 비즈니스에 중요한 부분이라 이부분을 도메인 계층으로 옮겨야할 것 같음
                .childMemoComments(memoComment.getParentMemoComment() == null ? childComments : null)
                .deleted(memoComment.isDeleted())
                .createdAt(memoComment.getCreatedAt())
                .updatedAt(memoComment.getUpdatedAt())
                .build();
    }
}
