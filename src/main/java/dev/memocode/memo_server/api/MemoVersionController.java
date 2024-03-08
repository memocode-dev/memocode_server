package dev.memocode.memo_server.api;

import dev.memocode.memo_server.api.spec.MemoVersionApi;
import dev.memocode.memo_server.domain.memo.dto.form.MemoCreateForm;
import dev.memocode.memo_server.domain.memo.dto.request.MemoCreateDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemoVersionDetailDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemoVersionsDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/memoVersion")
public class MemoVersionController implements MemoVersionApi {

    @PostMapping
    public ResponseEntity<String> createMemoVersion(@RequestBody MemoCreateForm form, @AuthenticationPrincipal Jwt jwt) {

        return ResponseEntity.created(null).build();
    }

    @DeleteMapping("/{memoVersionId}")
    public ResponseEntity<Void> deleteMemoVersion(@PathVariable UUID memoVersionId, @AuthenticationPrincipal Jwt jwt){

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{memoVersionId}")
    public ResponseEntity<MemoVersionDetailDTO> findMemoVersion(@PathVariable UUID memoVersionId, @AuthenticationPrincipal Jwt jwt){

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<MemoVersionsDTO> findAllMemoVersion(@AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.ok().build();
    }

}
