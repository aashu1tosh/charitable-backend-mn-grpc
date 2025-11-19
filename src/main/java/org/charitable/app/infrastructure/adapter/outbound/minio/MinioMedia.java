package org.charitable.app.infrastructure.adapter.outbound.minio;

import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.charitable.app.domain.port.outbound.media.MediaStoragePort;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;

@Singleton
@Slf4j
class MinioMedia implements MediaStoragePort {

    private final S3Client s3Client;
    private final S3Presigner presigner;

    public MinioMedia(S3Client s3Client, S3Presigner presigner) {
        this.s3Client = s3Client;
        this.presigner = presigner;
    }

    private void setPublicReadPolicy(String bucketName) {
        String policy = String.format("""
            {
              "Version": "2012-10-17",
              "Statement": [
                {
                  "Effect": "Allow",
                  "Principal": { "AWS": "*" },
                  "Action": "s3:GetObject",
                  "Resource": "arn:aws:s3:::%s/*"
                }
              ]
            }
            """, bucketName);

        PutBucketPolicyRequest putBucketPolicyRequest = PutBucketPolicyRequest.builder()
                .bucket(bucketName)
                .policy(policy)
                .build();

        try {
            s3Client.putBucketPolicy(putBucketPolicyRequest);
            System.out.println("SUCCESS: Public read policy applied to bucket: " + bucketName);
        } catch (S3Exception e) {
            // ⭐ Log the error to see why MinIO denied the policy change!
            System.err.println("CRITICAL: Failed to set public policy on bucket " + bucketName + ": " + e.getMessage());
            throw new RuntimeException("Failed to apply public policy", e);
        }
    }

    private void ensureBucketExists(String bucketName) {
        try {
            log.info("Attempting to check if bucket exists on bucket: " + bucketName);
            HeadBucketRequest headBucketRequest = HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.headBucket(headBucketRequest);
        } catch (NoSuchBucketException e) {
            log.info("Bucket Exception {}", bucketName);
            // 1. Create the bucket
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.createBucket(createBucketRequest);

            // 2. Set the policy to make it publicly readable
            setPublicReadPolicy(bucketName);
        }
        setPublicReadPolicy(bucketName);
    }

    private String getPresignedUrl(String bucketName, String filename) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                // Set the duration the URL will be valid for (e.g., 24 hours)
                .signatureDuration(Duration.ofHours(24))
                .getObjectRequest(getObjectRequest)
                .build();

        return presigner.presignGetObject(presignRequest).url().toString();
    }

//    @Override
//    public String uploadImage(String bucketName, String filename, InputStream content, String contentType) {
//        try {
//            ensureBucketExists(bucketName);
//            PutObjectRequest request = PutObjectRequest.builder()
//                    .bucket(bucketName)
//                    .key(filename)
//                    .contentType(contentType)
//                    .build();
//
//            long contentLength = content.available();
//            s3Client.putObject(request, RequestBody.fromInputStream(content, contentLength));
//
//            // *** NEW: Return the presigned URL instead of the simple path ***
//            return getPresignedUrl(bucketName, filename);
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to upload image", e);
//        }
//    }

    @Override
    public String uploadImage(String bucketName, String filename, InputStream content, String contentType, Long contentLength) {
        try {
            ensureBucketExists(bucketName);
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filename)
                    .contentType(contentType)
                    .contentLength(contentLength)
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(content, contentLength));

//            return String.format(bucketName, filename);
            return bucketName +'/' + filename;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    @Override
    public void deleteImage(String bucketName, String filename) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .build();

        s3Client.deleteObject(request);
    }
}