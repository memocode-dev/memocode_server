package dev.memocode.memo_server.domain.memocomment.dto.response;


import dev.memocode.memo_server.domain.author.dto.AuthorDTO;
import dev.memocode.memo_server.domain.memocomment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentsDTO {

    private UUID id;
    private String content;
    private Instant createAt;
    private Instant updateAt;
    private AuthorDTO authorDTO;
    @Builder.Default
    private List<ChildCommentsDTO> reply = new ArrayList<>();

    public static CommentsDTO from(Comment comment){
        List<ChildCommentsDTO> childComments = comment.getChildComments()
                .stream()
                .map(ChildCommentsDTO::from)
                .sorted(Comparator.comparing(ChildCommentsDTO::getCreateAt))
                .toList();

        return CommentsDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createAt(comment.getCreatedAt())
                .updateAt(comment.getUpdatedAt())
                .authorDTO(AuthorDTO.builder()
                        .authorId(comment.getAuthor().getId())
                        .nickname(comment.getAuthor().getNickname())
                        .username(comment.getAuthor().getUsername())
                        .build())
                .reply(childComments)
                .build();
    }
}
