package dev.memocode.application.memo.dto;

import dev.memocode.domain.core.BusinessRuleViolationException;
import lombok.Getter;

import static dev.memocode.domain.memo.MemoDomainErrorCode.INVALID_MEMO_IMAGE_TYPE;

@Getter
public enum AllowedImageMemoType {
    JPEG("image/jpeg", "jpeg"),
    PNG("image/png", "png"),
    GIF("image/gif", "gif");

    private final String mimeType;
    private final String extension;

    AllowedImageMemoType(String mimeType, String extension) {
        this.mimeType = mimeType;
        this.extension = extension;
    }

    public static AllowedImageMemoType toAllowedImageMemoType(String mimeType) {
        for (AllowedImageMemoType type : values()) {
            if (type.getMimeType().equalsIgnoreCase(mimeType)) {
                return type;
            }
        }

        throw new BusinessRuleViolationException(INVALID_MEMO_IMAGE_TYPE);
    }

    public static AllowedImageMemoType toAllowedImageMemoTypeFromExtension(String extension) {
        for (AllowedImageMemoType type : values()) {
            if (type.getExtension().equalsIgnoreCase(extension)) {
                return type;
            }
        }

        throw new BusinessRuleViolationException(INVALID_MEMO_IMAGE_TYPE);
    }
}
