package org.charitable.app.infrastructure.adapter.inbound.rest.media;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Part;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.AllArgsConstructor;
import org.charitable.app.application.dto.response.AppResponse;
import org.charitable.app.application.exception.AppException;
import org.charitable.app.application.port.inbound.media.MediaUseCase;
import org.charitable.app.common.utils.StringUtils;
import org.charitable.app.domain.model.media.BucketConstants;

import java.io.IOException;
import java.io.InputStream;

@Controller("/media")
@AllArgsConstructor
@Secured(SecurityRule.IS_ANONYMOUS)
public class MediaController {

    private final MediaUseCase service;

    @Post(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA)
    public AppResponse<String> uploadImage(@Part("file") CompletedFileUpload file, @Part("bucket") String bucket) {

        if(StringUtils.isEmpty(bucket) || !BucketConstants.ALL_BUCKETS.contains(bucket)) {
            throw AppException.badRequest("Bucket is not a valid bucket");
        }

        try (InputStream inputStream = file.getInputStream()) {
            var url =  service.uploadDonationImage(
                    "user123",
                    bucket,
                    file.getFilename(),
                    inputStream,
                    file.getContentType().toString()
            );
            return new AppResponse<String>(true, "Uploaded Successfully", url);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read uploaded file", e);
        }
    }
}
