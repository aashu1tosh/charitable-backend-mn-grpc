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


    @Override
    public String uploadDonationImage(String authId, String bucketName, String imageName, InputStream content, String contentType, Long contentLength) {
        String filename = authId + "/" + imageName;
        return storagePort.uploadImage(bucketName, filename, content, contentType, contentLength);
    }
}
