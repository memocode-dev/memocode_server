package dev.memocode.application.memo.repository;

import dev.memocode.application.memo.dto.AllowedImageMemoType;
import dev.memocode.application.memo.dto.result.CreateMemoImage_MemoImageResult;

import java.util.UUID;

public interface ImageMemoRepository {
    CreateMemoImage_MemoImageResult createMemoImageUploadURL(UUID userId, UUID memoId, AllowedImageMemoType allowedImageMemoType);
    String findMemoImageUploadURL(UUID userId, UUID memoId, UUID memoImageId, AllowedImageMemoType allowedImageMemoType);
}
