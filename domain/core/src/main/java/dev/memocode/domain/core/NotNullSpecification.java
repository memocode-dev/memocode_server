package dev.memocode.domain.core;

public class NotNullSpecification<T> implements Specification<T> {
    @Override
    public boolean isSatisfiedBy(T candidate) {
        return candidate != null;
    }
}
