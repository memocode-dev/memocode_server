package dev.memocode.memo_server.domain.base.config;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;

public class MeilisearchRuntimeHintsRegistrar implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection().registerType(TypeReference.of("com.meilisearch.sdk.Index"), hint -> {
            hint.withMembers(MemberCategory.INVOKE_PUBLIC_METHODS);
            hint.withMembers(MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS);
            hint.withMembers(MemberCategory.PUBLIC_FIELDS);
        });
    }
}
