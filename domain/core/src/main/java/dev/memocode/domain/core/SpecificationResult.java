package dev.memocode.domain.core;

import lombok.Getter;

@Getter
public class SpecificationResult {
    private final boolean satisfied;
    private final String message;

    private SpecificationResult(boolean satisfied, String message) {
        this.satisfied = satisfied;
        this.message = message;
    }

    public static SpecificationResult satisfied() {
        return new SpecificationResult(true, "");
    }

    public static SpecificationResult notSatisfied(String message) {
        return new SpecificationResult(false, message);
    }

}
