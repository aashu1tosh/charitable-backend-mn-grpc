package org.charitable.app.infrastructure.adapter.outbound.jpa.donation.donationStatusHistory;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.charitable.app.domain.model.donation.DonationStatus;
import org.charitable.app.infrastructure.adapter.outbound.jpa.base.BaseEntity;
import org.charitable.app.infrastructure.adapter.outbound.jpa.donation.DonationEntity;

@Entity
@Table(name = "donation_status_history")
@Getter
@Setter
public class DonationStatusHistory extends BaseEntity {

    @Column(name = "status", nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private DonationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donation_id", nullable = false)
    private DonationEntity donation;
}
