package org.charitable.app.infrastructure.adapter.outbound.jpa.donation;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.charitable.app.domain.model.donation.DonationStatus;
import org.charitable.app.domain.model.donation.DonationType;
import org.charitable.app.infrastructure.adapter.outbound.jpa.auth.AuthEntity;
import org.charitable.app.infrastructure.adapter.outbound.jpa.base.BaseEntity;
import org.charitable.app.infrastructure.adapter.outbound.jpa.organization.OrganizationEntity;

@Entity
@Table(name = "donation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonationEntity extends BaseEntity {

    @Column()
    @NotNull
    private String title;

    @Column()
    @NotNull
    private String description;

    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private DonationType type;

    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private DonationStatus status;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "donor_auth_id", nullable = true)
    private AuthEntity donor;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "organization_id", nullable = true)
    private OrganizationEntity organization;

}
