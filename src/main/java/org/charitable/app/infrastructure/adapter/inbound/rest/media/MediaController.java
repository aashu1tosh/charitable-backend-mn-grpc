package org.charitable.app.infrastructure.adapter.inbound.rest.media;

import io.micronaut.http.HttpMethod;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Part;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.http.server.cors.CrossOrigin;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.charitable.app.application.dto.response.AppResponse;
import org.charitable.app.application.exception.AppException;
import org.charitable.app.application.port.inbound.media.MediaUseCase;
import org.charitable.app.common.utils.StringUtils;
import org.charitable.app.domain.model.media.BucketConstants;

import java.io.IOException;
import java.io.InputStream;

//@CrossOrigin(
//        allowedOrigins = {"http://localhost:3000"},
//        allowedMethods = {HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS},
//        allowCredentials = true
//)
@Controller("/media")
@AllArgsConstructor
@Secured(SecurityRule.IS_ANONYMOUS)
@Slf4j
public class MediaController {

    private final MediaUseCase service;

    @Post(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA)
    public AppResponse<String> uploadImage(@Part("file") CompletedFileUpload file, @Part("bucket") String bucket) {
        log.info("MediaController hit uploadImage");
        if(StringUtils.isEmpty(bucket) || !BucketConstants.ALL_BUCKETS.contains(bucket)) {
            throw AppException.badRequest("Bucket is not a valid bucket");
        }
        String contentType = file.getContentType()
                .map(MediaType::getName)
                .orElse("image/jpeg");

        try (InputStream inputStream = file.getInputStream()) {
            var url =  service.uploadDonationImage(
                    "user123",
                    bucket,
                    file.getFilename(),
                    inputStream,
                    contentType,
                    file.getSize()
            );
            log.info("Uploaded image from {} to {}", url, file.getFilename());
            return new AppResponse<String>(true, "Uploaded Successfully", url);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read uploaded file", e);
        }
    }
}
