package dev.memocode.memo_server.mapper;

import dev.memocode.memo_server.dto.form.MemoCreateForm;
import dev.memocode.memo_server.dto.request.MemoCreateDTO;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MemoCreateDTOMapper {

    public MemoCreateDTO fromMemoCreateFormAndAccountId(MemoCreateForm form, UUID accountId) {
        return MemoCreateDTO.builder()
                .title(form.getTitle())
                .content(form.getContent())
                .accountId(accountId)
                .build();
    }
}
