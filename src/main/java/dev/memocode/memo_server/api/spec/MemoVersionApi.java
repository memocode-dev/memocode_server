package dev.memocode.memo_server.api.spec;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "memo_version", description = "메모 버전 API")
@SecurityRequirement(name = "bearer-key")
public interface MemoVersionApi {

}
