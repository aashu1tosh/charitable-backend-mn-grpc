package org.charitable.app.domain.port.outbound.media;

import java.io.InputStream;

public interface MediaStoragePort {
    /**
     * Uploads an image to storage.
     *
     * @param bucketName    The name of the bucket
     * @param filename    The path or name to store the image under
     * @param content     InputStream of the image content
     * @param contentType MIME type of the image (e.g., "image/png")
     * @return The path or URL of the uploaded image
     */
    String uploadImage(String bucketName, String filename, InputStream content, String contentType, Long contentLength);

    /**
     * Deletes an image from storage.
     *
     * @param bucketName    The name of the bucket
     * @param filename The path or name of the image to delete
     */
    void deleteImage(String bucketName, String filename);
}
