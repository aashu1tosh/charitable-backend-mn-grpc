package org.charitable.app.application.dto.request.donation;

import io.micronaut.core.annotation.Introspected;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.charitable.app.common.validation.uuid.UUIDCheck;

import java.util.UUID;

@Getter
@Setter
@Introspected
@Builder
public class ClaimDonationRequestDTO {

    @NotNull
    private UUID id;
}
