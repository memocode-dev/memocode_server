package dev.memocode.memo_server.api.spec;

import dev.memocode.memo_server.dto.form.MemoCreateForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

@Tag(name = "memo_version", description = "메모 버전 API")
@SecurityRequirement(name = "bearer-key")
public interface MemoVersionApi {

}
