package org.charitable.app.infrastructure.adapter.outbound.minio;

import jakarta.inject.Singleton;
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
class MinioMedia implements MediaStoragePort {

    private final S3Client s3Client;
    private final S3Presigner presigner;

    public MinioMedia(S3Client s3Client, S3Presigner presigner) {
        this.s3Client = s3Client;
        this.presigner = presigner;
    }

    private void setPublicReadPolicy(String bucketName) {
        // This is the JSON policy required to grant s3:GetObject (read) access to anyone (*)
        String policy = String.format("""
            {
                "Version": "2012-10-17",
                "Statement": [
                    {
                        "Effect": "Allow",
                        "Principal": "*",
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

        s3Client.putBucketPolicy(putBucketPolicyRequest);
    }

    private void ensureBucketExists(String bucketName) {
        try {
            HeadBucketRequest headBucketRequest = HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.headBucket(headBucketRequest);
        } catch (NoSuchBucketException e) {
            // 1. Create the bucket
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.createBucket(createBucketRequest);

            // 2. Set the policy to make it publicly readable
            setPublicReadPolicy(bucketName);
        }
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
    public String uploadImage(String bucketName, String filename, InputStream content, String contentType) {
        try {
            ensureBucketExists(bucketName);
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filename)
                    .contentType(contentType)
                    .build();

            long contentLength = content.available();
            s3Client.putObject(request, RequestBody.fromInputStream(content, contentLength));

            return String.format(bucketName, filename);

        } catch (IOException e) {
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