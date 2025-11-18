package org.charitable.app.infrastructure.adapter.inbound.rest.media;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Part;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.AllArgsConstructor;
import org.charitable.app.application.port.inbound.media.MediaUseCase;

import java.io.IOException;
import java.io.InputStream;

@Controller("/media")
@AllArgsConstructor
@Secured(SecurityRule.IS_ANONYMOUS)
public class MediaController {

    private final MediaUseCase service;

    @Post(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA)
    public String uploadImage(@Part CompletedFileUpload file) {
        try (InputStream inputStream = file.getInputStream()) {
            // You can replace "user123" with actual user ID dynamically
            return service.uploadDonationImage(
                    "user123",
                    file.getFilename(),
                    inputStream,
                    file.getContentType().toString()
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to read uploaded file", e);
        }
    }
}
