package dev.memocode.domain.core;

public interface Specification<T> {
    boolean isSatisfiedBy(T candidate);

    default Specification<T> and(Specification<T> other) {
        return (t) -> this.isSatisfiedBy(t) && other.isSatisfiedBy(t);
    }
}
