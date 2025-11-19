package org.charitable.app.application.service.media;

import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import org.charitable.app.application.port.inbound.media.MediaUseCase;
import org.charitable.app.domain.port.outbound.media.MediaStoragePort;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;

import java.io.InputStream;

@Singleton
@AllArgsConstructor
class MediaService implements MediaUseCase {

    private final MediaStoragePort storagePort;
    private final S3Client s3Client;

    private void ensureBucketExists(String bucketName) {
        try {
            HeadBucketRequest headBucketRequest = HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.headBucket(headBucketRequest);
        } catch (NoSuchBucketException e) {
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.createBucket(createBucketRequest);
        }
    }

    @Override
    public String uploadDonationImage(String authId, String bucketName, String imageName, InputStream content, String contentType) {
        String filename = authId + "/" + imageName;
        return storagePort.uploadImage(bucketName, filename, content, contentType);
    }
}
