package org.charitable.app.application.dto.request.donation;

import io.micronaut.core.annotation.Introspected;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.charitable.app.domain.model.donation.DonationType;

@Introspected
@Getter
@Setter
@Builder
public class DonateRequestDTO {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Donation Type is required")
    private DonationType donationType;

    private String url;
}
