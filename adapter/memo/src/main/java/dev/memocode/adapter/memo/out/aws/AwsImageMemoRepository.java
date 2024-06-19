package dev.memocode.adapter.memo.out.aws;

import dev.memocode.application.memo.dto.AllowedImageMemoType;
import dev.memocode.application.memo.dto.result.CreateMemoImage_MemoImageResult;
import dev.memocode.application.memo.repository.ImageMemoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Repository
public class AwsImageMemoRepository implements ImageMemoRepository {

    @Value("${custom.s3.bucket_name}")
    private String BUCKET_NAME;

    private final static long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    private final static String IMAGE_MEMO_KEY_TEMPLATE = "users/%s/memos/%s/images/%s.%s";

    @Override
    public CreateMemoImage_MemoImageResult createMemoImageUploadURL(UUID userId, UUID memoId,
                                                                    AllowedImageMemoType allowedImageMemoType) {
        try (
                S3Presigner presigner = S3Presigner.create()
        ) {
            UUID memoImageId = UUID.randomUUID();

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(IMAGE_MEMO_KEY_TEMPLATE.formatted(
                            userId,
                            memoId,
                            memoImageId,
                            allowedImageMemoType.getExtension())
                    )
                    .contentType(allowedImageMemoType.getMimeType())
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(5))
                    .putObjectRequest(objectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);

            return CreateMemoImage_MemoImageResult.builder()
                    .memoImageId(memoImageId)
                    .extension(allowedImageMemoType.getExtension())
                    .uploadURL(presignedRequest.url().toExternalForm())
                    .build();
        }
    }

    @Override
    public String findMemoImageUploadURL(UUID userId, UUID memoId, UUID memoImageId, AllowedImageMemoType allowedImageMemoType) {
        try (
                S3Presigner presigner = S3Presigner.create()
        ) {
            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(IMAGE_MEMO_KEY_TEMPLATE.formatted(
                            userId,
                            memoId,
                            memoImageId,
                            allowedImageMemoType.getExtension())
                    )
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(5))
                    .getObjectRequest(objectRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);

            String presignedURL = presignedRequest.url().toExternalForm();

            return presignedURL;
        }
    }
}
