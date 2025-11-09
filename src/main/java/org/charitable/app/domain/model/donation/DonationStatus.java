package org.charitable.app.domain.model.donation;

public enum DonationStatus {
    AVAILABLE,
    CLAIMED,
    DONATED,      // Received by the organization
    CANCELLED       // Donation was cancelled or withdrawn
}
