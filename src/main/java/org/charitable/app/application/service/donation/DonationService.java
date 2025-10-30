package org.charitable.app.application.service.donation;


import jakarta.inject.Singleton;
import org.charitable.app.application.exception.AppException;
import org.charitable.app.application.port.inbound.donation.DonationUseCase;
import org.charitable.app.domain.entity.donation.Donation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DonationService implements DonationUseCase {
    private static final Logger logger = LoggerFactory.getLogger(DonationService.class);

    @Override
    public Donation donate(Object admin) {
        throw AppException.badRequest("Method not Implemented");
    }
}
