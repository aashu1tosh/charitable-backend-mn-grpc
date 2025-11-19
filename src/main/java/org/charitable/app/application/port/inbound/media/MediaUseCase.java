package org.charitable.app.application.port.inbound.media;

import java.io.InputStream;

public interface MediaUseCase {
    public String uploadDonationImage(String authId, String bucketName, String imageName, InputStream content, String contentType);
}
