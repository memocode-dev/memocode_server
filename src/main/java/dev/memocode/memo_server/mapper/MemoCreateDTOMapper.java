package dev.memocode.memo_server.mapper;

import dev.memocode.memo_server.dto.form.MemoCreateForm;
import dev.memocode.memo_server.dto.form.MemoUpdateForm;
import dev.memocode.memo_server.dto.request.MemoCreateDTO;
import dev.memocode.memo_server.dto.request.MemoDeleteDTO;
import dev.memocode.memo_server.dto.request.MemoUpdateDTO;
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

    public MemoDeleteDTO fromMemoDeleteMemoIdAndAccountId(UUID memoId, UUID accountId) {
        return MemoDeleteDTO.builder()
                .memoId(memoId)
                .accountId(accountId)
                .build();
    }

    public MemoUpdateDTO fromMemoUpdateMemoIdAndAccountId(UUID memoId, MemoUpdateForm form, UUID accountId) {
        return MemoUpdateDTO.builder()
                .memoId(memoId)
                .accountId(accountId)
                .title(form.getTitle())
                .content(form.getContent())
                .build();
    }
}
