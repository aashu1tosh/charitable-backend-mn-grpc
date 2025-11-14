package org.charitable.app.infrastructure.adapter.outbound.jpa.donation;

import io.micronaut.data.annotation.Repository;
import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.charitable.app.domain.common.pagination.Page;
import org.charitable.app.domain.common.pagination.Pagination;
import org.charitable.app.domain.entity.donation.Donation;
import org.charitable.app.domain.entity.organization.Organization;
import org.charitable.app.domain.model.donation.DonationFilter;
import org.charitable.app.domain.model.donation.DonationStatus;
import org.charitable.app.domain.model.token.TokenPayload;
import org.charitable.app.domain.port.outbound.donation.DonationRepository;
import org.charitable.app.infrastructure.adapter.outbound.jpa.auth.AuthEntity;
import org.charitable.app.infrastructure.adapter.outbound.jpa.organization.OrganizationEntity;
import org.charitable.app.infrastructure.mapper.auth.AuthMapper;
import org.charitable.app.infrastructure.mapper.donation.DonationMapper;
import org.charitable.app.infrastructure.mapper.organization.OrganizationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
@Repository
class DonationRepositoryImpl implements DonationRepository {

    private static final Logger logger = LoggerFactory.getLogger(DonationRepositoryImpl.class);

    private final DonationJpaRepository donationJpaRepo;
    private final EntityManager entityManager;

    DonationRepositoryImpl(DonationJpaRepository donationJpaRepo, EntityManager entityManager) {
        this.donationJpaRepo = donationJpaRepo;
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Donation> findById(UUID id) {
         return donationJpaRepo.findById(id)
                 .map(DonationMapper::mapToDomain);
    }

    @Override
    public Optional<Donation> findByIdWithRelations(UUID id) {
        return donationJpaRepo.findByIdWithRelations(id)
                .map(DonationMapper::mapToDomain);
    }

    @Override
    public Donation save(Donation donation) {
        var entity = DonationEntity.builder()
                .title(donation.getTitle())
                .description(donation.getDescription())
                .type(donation.getType())
                .status(donation.getStatus())
                .latitude(donation.getLatitude())
                .longitude(donation.getLongitude())
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

        // --- Joins ---
        Join<DonationEntity, AuthEntity> donorJoin = donation.join("donor", JoinType.LEFT);
        Join<DonationEntity, OrganizationEntity> organizationJoin = donation.join("organization", JoinType.LEFT);

        // --- Predicates ---
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getSearch() != null && !filter.getSearch().isEmpty()) {
            String pattern = "%" + filter.getSearch().toLowerCase() + "%";
            predicates.add(cb.or(
                    cb.like(cb.lower(donation.get("title")), pattern),
                    cb.like(cb.lower(donation.get("description")), pattern)
            ));
        }

        if (filter.getType() != null) {
            predicates.add(cb.equal(donation.get("type"), filter.getType()));
        }

        if (filter.getStatus() != null) {
            predicates.add(cb.equal(donation.get("status"), filter.getStatus()));
        }

        if (user != null && user.getUserId() != null) {
            predicates.add(cb.equal(donorJoin.get("id"), user.getId()));
            predicates.add(cb.equal(organizationJoin.get("id"), user.getUserId()));
        } else {
            predicates.add(cb.isNull(organizationJoin.get("id")));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.desc(donation.get("createdAt")));

        // --- Pagination ---
        int page = filter.getPage();
        int limit = filter.getLimit();
        int offset = (page - 1) * limit;

        List<DonationEntity> resultList = entityManager.createQuery(cq)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

        // --- Count query ---
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<DonationEntity> countRoot = countQuery.from(DonationEntity.class);
        Join<DonationEntity, AuthEntity> countDonorJoin = countRoot.join("donor", JoinType.LEFT);
        Join<DonationEntity, OrganizationEntity> countOrgJoin = countRoot.join("organization", JoinType.LEFT);

        List<Predicate> countPredicates = new ArrayList<>();

        if (filter.getSearch() != null && !filter.getSearch().isEmpty()) {
            String pattern = "%" + filter.getSearch().toLowerCase() + "%";
            countPredicates.add(cb.or(
                    cb.like(cb.lower(countRoot.get("title")), pattern),
                    cb.like(cb.lower(countRoot.get("description")), pattern)
            ));
        }

        if (filter.getType() != null) {
            countPredicates.add(cb.equal(countRoot.get("type"), filter.getType()));
        }

        if (filter.getStatus() != null) {
            countPredicates.add(cb.equal(countRoot.get("status"), filter.getStatus()));
        }

        if (user != null && user.getUserId() != null) {
            countPredicates.add(cb.equal(countDonorJoin.get("id"), user.getId()));
            countPredicates.add(cb.equal(countOrgJoin.get("id"), user.getUserId()));
        } else {
            countPredicates.add(cb.isNull(countOrgJoin.get("id")));
        }

        countQuery.select(cb.count(countRoot))
                .where(countPredicates.toArray(new Predicate[0]));

        int total = entityManager.createQuery(countQuery).getSingleResult().intValue();
        int totalPages = (int) Math.ceil((double) total / limit);

        List<Donation> donations = resultList.stream()
                .map(DonationMapper::mapToDomain)
                .toList();

        var pagination = Pagination.builder()
                .page(page)
                .limit(limit)
                .total(total)
                .totalPages(totalPages)
                .build();

        return new Page<>(donations, pagination);
    }

    @Transactional
    @Override
    public Donation claimDonation(UUID id, Organization organization) {
        DonationEntity donation = entityManager.find(
                DonationEntity.class,
                id,
                LockModeType.OPTIMISTIC
        );

        if(donation.getOrganization() != null) {
            throw new IllegalStateException("Already claimed");
        }

        donation.setOrganization(OrganizationMapper.mapToEntitySafe(organization));
        entityManager.persist(donation);

        return DonationMapper.mapToDomain(donation);
    }

    @Transactional
    @Override
    public Donation gotDonation(UUID id) {

        DonationEntity donation = entityManager.find(DonationEntity.class, id);
        if(donation.getOrganization() == null) {
            throw new IllegalStateException("Claim first");
        }

        donation.setStatus(DonationStatus.DONATED);
        entityManager.persist(donation);
        return DonationMapper.mapToDomain(donation);
    }

}
