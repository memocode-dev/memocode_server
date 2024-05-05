package dev.memocode.domain.memo;

import dev.memocode.domain.core.*;
import dev.memocode.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static dev.memocode.domain.memo.MemoDomainErrorCode.*;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.*;

@Getter
@Entity
@SuperBuilder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Table(name = "memos")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Memo extends SoftDeleteBaseEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "summary")
    private String summary;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "visibility")
    private Boolean visibility;

    @Column(name = "visibility_achieved_at")
    private Instant visibilityAchievedAt;

    @Column(name = "bookmarked")
    private Boolean bookmarked;

    @Column(name = "security")
    private Boolean security;

    @OneToMany(mappedBy = "memo", cascade = {CascadeType.PERSIST})
    @Builder.Default
    private List<MemoVersion> memoVersions = new ArrayList<>();

    @Getter(value = NONE)
    @OneToMany(mappedBy = "memo", cascade = {CascadeType.PERSIST})
    @Builder.Default
    private List<MemoComment> memoComments = new ArrayList<>();

    @Transient
    @Setter
    private Memo formattedMemo;

    /**
     * 현재 메모 중에서 변경할 수 있는 필드를 변경합니다.
     *  - 변경할 수 있는 필드
     *      - title
     *      - content
     *      - summary
     *      - bookmarked
     *      - security
     *      - visibility
     *  - dto 필드 중에 null인 필드의 경우에는 수정을 하지 않고 null이 아닌 필드의 경우에만 수정이 일어남
     * @param dto 변경하고자 하는 필드의 값들
     */
    protected void change(MemoUpdateDomainDTO dto) {
        String title = dto.getTitle();
        String content = dto.getContent();
        String summary = dto.getSummary();
        Boolean bookmarked = dto.getBookmarked();
        Boolean security = dto.getSecurity();
        Boolean visibility = dto.getVisibility();

        this.title = title != null ? title : this.title;
        this.content = content != null ? content : this.content;
        this.summary = summary != null ? summary : this.summary;
        this.bookmarked = bookmarked != null ? bookmarked : this.bookmarked;

        if (security != null && visibility != null) {
            // 동시에 security와 visibility를 수정할 수 없음
            throw new ValidationException(CANNOT_UPDATE_SECURITY_AND_VISIBILITY_TOGETHER);
        } else if (security != null) {
            // 한번 보호 모드가 작동한다면 security를 변경하지 못함
            if (this.security) {
                throw new BusinessRuleViolationException(PROTECT_MEMO_SECURITY_UNMODIFIED);
            }

            // 이미 한번 공개되었다면 보호모드로 변경 못함
            if (this.visibilityAchievedAt != null) {
                throw new BusinessRuleViolationException(PROTECT_MODE_DISABLED_ONCE_PUBLIC);
            }

            this.security = security;
        } else if (visibility != null) {
            // 한번 보호 모드가 작동한다면 visibility 필드 사용불가
            if (this.security) {
                throw new BusinessRuleViolationException(PROTECT_MEMO_VISIBILITY_UNMODIFIED);
            }

            this.visibility = visibility;
            this.visibilityAchievedAt = Instant.now();
        }
    }

    /**
     * 메모를 소프트 삭제합니다.
     *  - 메모가 삭제될 때 메모버전과 메모댓글들이 모두 삭제될 수 있도록 로직 구성
     */
    protected void softDelete() {
        super.delete();

        memoVersions.forEach(MemoVersion::softDelete);
        // TODO comment의 개수가 많아질 때는 DB에서 직접 삭제 요청 필요
        memoComments.forEach(MemoComment::softDelete);
    }

    /**
     * 자신의 메모인지 확인합니다.
     *  - 자신의 메모가 아닌 경우 exception 발생
     * @param user 작성자
     */
    protected void assertIsMemoOwner(User user) {
        if (!this.isMemoOwner(user)) {
            throw new ForbiddenException(NOT_MEMO_OWNER);
        }
    }

    /**
     * 메모가 삭제되지 않았는지 확인합니다.
     *  - 삭제되지 않았다면 통과, 삭제되었다면 exception 발생
     */
    protected void assertIsNotDeleted() {
        if (this.getDeleted()) {
            throw new ForbiddenException(DELETED_MEMO);
        }
    }

    /**
     * 공개된 메모인지 확인합니다.
     *  - 공개되었으면 통과, 공개되지 않았으면 exception 발생
     */
    protected void assertIsVisibility() {
        if (!this.visibility) {
            throw new BusinessRuleViolationException(NOT_VISIBILITY_MEMO);
        }
    }

    /**
     * 자신의 메모인지 확인합니다.
     * @param user 자신의 메모가 맞는지 확인할 유저
     * @return 자신의 메모가 맞다면 true 반환
     */
    protected boolean isMemoOwner(User user) {
        return this.getUser().equals(user);
    }

    /**
     * 메모버전을 생성하여 반환합니다.
     *  - 모든 메모버전의 개수를 구해 version을 명시
     * @return MemoVersion 생성된 메모 버전
     */
    protected MemoVersion addVersion() {
        MemoVersion memoVersion = MemoVersion.builder()
                .id(UUID.randomUUID())
                .memo(this)
                .content(this.content)
                .version(this.memoVersions.size())
                .deleted(false)
                .build();

        this.memoVersions.add(memoVersion);

        return memoVersion;
    }

    /**
     * 메모버전을 소프트 삭제합니다.
     * @param memoVersion 삭제하고자 하는 메모 버전
     */
    protected void removeVersion(MemoVersion memoVersion) {
        memoVersion.softDelete();
    }

    /**
     *  memoVersionId를 가진 메모 버전을 찾아 반환합니다.
     *
     * @param memoVersionId 찾고자 하는 메모 버전 id
     * @return 메모 버전 id를 통해 찾은 메모 버전
     * @throws NotFoundException 메모 버전을 찾을 수 없을 경우 발생합니다. 이는 주어진 {@code memoVersionId}에 해당하는 메모 버전이 존재하지 않을 때 발생합니다.
     */
    protected MemoVersion findMemoVersionById(UUID memoVersionId) {
        return this.memoVersions.stream().filter(mv -> mv.getId().equals(memoVersionId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(MEMO_VERSION_NOT_FOUND));
    }

    /**
     *
     * @param content 변경하고자 하는 메모 댓글 내용
     * @param user 메모 댓글을 생성하고자 하는 유저
     * @return 생성된 메모를 반환
     */
    protected MemoComment addComment(String content, User user) {
        MemoComment comment = MemoComment.builder()
                .id(UUID.randomUUID())
                .memo(this)
                .user(user)
                .content(content)
                .deleted(false)
                .build();

        this.addFilterdComment(comment);

        return comment;
    }

    /**
     * 메모 댓글의 내용을 변경합니다.
     * @param memoComment 내용을 변경하고자 하는 메모 댓글
     * @param content 변경하고자 하는 내용
     */
    protected void updateComment(MemoComment memoComment, String content) {
        memoComment.changeContent(content);
    }

    /**
     * 메모 댓글을 소프트 삭제합니다.
     * @param memoComment 삭제하고자 하는 메모 댓글
     */
    protected void removeComment(MemoComment memoComment) {
        memoComment.softDelete();
    }

    /**
     * 메모댓글의 메모답글을 생성합니다.
     * @param parentMemoComment 답글을 작성하고자 하는 댓글
     * @param user 작성자
     * @param content 답글 내용
     * @return 답글 반환
     */
    protected MemoComment addChildComment(MemoComment parentMemoComment, User user, String content) {
        return parentMemoComment.addChildComment(user, content);
    }

    /**
     * 제일 상위에 있는 메모 댓글만 출력
     * @return parentMemoComment가 null인 comment만 출력
     */
    protected List<MemoComment> getMemoComments() {
        return memoComments.stream()
                .filter(comment -> comment.getParentMemoComment() == null)
                .toList();
    }

    /**
     * 메모를 필터링하여 추가합니다.
     */
    protected void addFilterdComment(MemoComment comment) {
        if (comment.getParentMemoComment() == null) this.memoComments.add(comment);
    }
}
