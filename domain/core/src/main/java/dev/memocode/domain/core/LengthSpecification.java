package dev.memocode.domain.core;

public class LengthSpecification<T extends String> implements Specification<T> {

    private final int min;
    private final int max;

    public LengthSpecification(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean isSatisfiedBy(T candidate) {
        if (candidate == null) return false;
        int length = candidate.length();
        return length >= min && length <= max;
    }
}
