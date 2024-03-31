package dev.memocode.memo_server.domain.base.config;

import jakarta.annotation.Nullable;
import org.hibernate.generator.GeneratorCreationContext;
import org.hibernate.generator.internal.CurrentTimestampGeneration;
import org.hibernate.id.uuid.UuidGenerator;
import org.springframework.aot.hint.ExecutableMode;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;

public class HibernateNativeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(@NonNull RuntimeHints hints, @Nullable ClassLoader classLoader) {
        try {
            Constructor<UuidGenerator> constructor1 =
                    ReflectionUtils.accessibleConstructor(UuidGenerator.class,
                            org.hibernate.annotations.UuidGenerator.class,
                            Member.class,
                            GeneratorCreationContext.class);
            hints.reflection().registerConstructor(constructor1, ExecutableMode.INVOKE);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
