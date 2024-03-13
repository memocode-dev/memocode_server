package dev.memocode.memo_server.domain.memo.mapper;

import dev.memocode.memo_server.domain.memo.dto.form.MemoCreateForm;
import dev.memocode.memo_server.domain.memo.dto.form.MemoUpdateForm;
import dev.memocode.memo_server.domain.memo.dto.request.MemoCreateDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoDeleteDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoUpdateDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoUpdateVisibilityDTO;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MemoDtoMapper {

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

    public MemoUpdateDTO fromMemoUpdate(UUID memoId, MemoUpdateForm form, UUID accountId) {
        return MemoUpdateDTO.builder()
                .memoId(memoId)
                .accountId(accountId)
                .title(form.getTitle())
                .content(form.getContent())
                .build();
    }

    public MemoUpdateVisibilityDTO fromMemoUpdateVisibility(UUID memoId, boolean visibility, UUID accountId) {
        return MemoUpdateVisibilityDTO.builder()
                .memoId(memoId)
                .visibility(visibility)
                .accountId(accountId)
                .build();
    }
}
