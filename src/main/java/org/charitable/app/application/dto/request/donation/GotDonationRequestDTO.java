package org.charitable.app.application.dto.request.donation;

import io.micronaut.core.annotation.Introspected;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@Introspected
@Builder
public class GotDonationRequestDTO {
    @NotNull
    private UUID id;
}
