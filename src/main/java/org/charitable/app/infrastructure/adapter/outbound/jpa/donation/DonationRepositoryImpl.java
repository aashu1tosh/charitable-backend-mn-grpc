package org.charitable.app.infrastructure.adapter.outbound.jpa.donation;

import io.micronaut.http.server.exceptions.NotFoundException;
import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.charitable.app.application.exception.AppException;
import org.charitable.app.domain.common.pagination.Page;
import org.charitable.app.domain.common.pagination.Pagination;
import org.charitable.app.domain.entity.donation.Donation;
import org.charitable.app.domain.model.donation.DonationFilter;
import org.charitable.app.domain.model.token.TokenPayload;
import org.charitable.app.domain.port.outbound.donation.DonationRepository;
import org.charitable.app.infrastructure.mapper.auth.AuthMapper;
import org.charitable.app.infrastructure.mapper.donation.DonationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Singleton
class DonationRepositoryImpl implements DonationRepository {

    private static final Logger logger = LoggerFactory.getLogger(DonationRepositoryImpl.class);

    private final DonationJpaRepository donationJpaRepo;
    private final EntityManager entityManager;

    DonationRepositoryImpl(DonationJpaRepository donationJpaRepo, EntityManager entityManager) {
        this.donationJpaRepo = donationJpaRepo;
        this.entityManager = entityManager;
    }

    @Override
    public Donation save(Donation donation) {
        var entity = DonationEntity.builder()
                .title(donation.getTitle())
                .description(donation.getDescription())
                .type(donation.getType())
                .status(donation.getStatus())
                .donor(AuthMapper.mapToEntity(donation.getDonor()))
                .url(donation.getUrl() != null ?  donation.getUrl() : null)
                .build();
        var resp = donationJpaRepo.save(entity);
        return DonationMapper.mapToDomain(resp);

    }

    @Transactional
    @ReadOnly
    @Override
    public Page<Donation> getDonations(DonationFilter filter, TokenPayload user) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<DonationEntity> cq = cb.createQuery(DonationEntity.class);
        Root<DonationEntity> donation = cq.from(DonationEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        // --- Search filter ---
        if (filter.getSearch() != null && !filter.getSearch().isEmpty()) {
            String pattern = "%" + filter.getSearch().toLowerCase() + "%";
            predicates.add(cb.or(
                    cb.like(cb.lower(donation.get("title")), pattern),
                    cb.like(cb.lower(donation.get("description")), pattern)
            ));
        }

        // --- DonationType filter ---
        if (filter.getType() != null) {
            predicates.add(cb.equal(donation.get("type"), filter.getType()));
        }

        // --- DonationStatus filter ---
        if (filter.getStatus() != null) {
            predicates.add(cb.equal(donation.get("status"), filter.getStatus()));
        }

        // --- Optional: filter by organization/donor if needed ---
        // e.g., donations by this user
        if (user != null && user.getUserId() != null) {
            predicates.add(cb.equal(donation.get("donor").get("id"), user.getId()));
        }

        // Apply predicates
        cq.where(predicates.toArray(new Predicate[0]));

        // Optional: order by created date
        cq.orderBy(cb.desc(donation.get("createdAt")));

        // --- Pagination ---
        int page = filter.getPage();
        int limit = filter.getLimit();
        int offset = (page - 1) * limit;

        List<DonationEntity> resultList = entityManager.createQuery(cq)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

        // --- Count total items for Page object ---
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<DonationEntity> countRoot = countQuery.from(DonationEntity.class);
        countQuery.select(cb.count(countRoot));
        countQuery.where(predicates.toArray(new Predicate[0]));

        int total = entityManager.createQuery(countQuery).getSingleResult().intValue();
        int totalPages = (int) Math.ceil((double) total / limit);


        List<Donation> donations = resultList.stream()
                .map(DonationMapper::mapToDomain)
                .toList();

        var pagination = Pagination.builder()
                .limit(page)
                .total(total)
                .totalPages(totalPages)
                .build();

        return new Page<Donation>(donations, pagination);
    }

    @Override
    public Donation claimDonation(UUID id, UUID organization) {
        throw new RuntimeException("Method not implemented");
    }
}
