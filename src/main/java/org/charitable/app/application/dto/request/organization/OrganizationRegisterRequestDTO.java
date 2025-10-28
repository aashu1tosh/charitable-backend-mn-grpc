package org.charitable.app.application.dto.request.organization;

import io.micronaut.core.annotation.Introspected;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Introspected
public class OrganizationRegisterRequestDTO {

    @NotBlank(message = "Organization name cannot be empty")
    private String name;

    @NotBlank(message = "Address cannot be empty")
    private String address;

    private Float latitude;

    private Float longitude;

    @NotBlank(message = "Government ID cannot be empty")
    private String govtId;

    @NotBlank(message = "Contact number cannot be empty")
    private String contactNumber;
}
