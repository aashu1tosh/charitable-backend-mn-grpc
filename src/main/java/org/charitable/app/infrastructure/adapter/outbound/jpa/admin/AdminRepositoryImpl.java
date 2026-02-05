package org.charitable.app.infrastructure.adapter.outbound.jpa.admin;

import jakarta.inject.Singleton;
import org.charitable.app.domain.entity.admin.Admin;
import org.charitable.app.domain.port.outbound.db.admin.AdminRepository;
import org.charitable.app.infrastructure.mapper.admin.AdminMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class AdminRepositoryImpl implements AdminRepository {
    private static final Logger logger = LoggerFactory.getLogger(AdminRepositoryImpl.class);

    private final AdminJpaRepository jpaRepository;

    AdminRepositoryImpl(AdminJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Admin register(Admin admin) {
        var entity = AdminEntity.builder()
                .firstName(admin.getFirstName())
                .middleName(admin.getMiddleName() != null ? admin.getMiddleName() : "")
                .lastName(admin.getLastName())
                .build();

        var savedEntity = jpaRepository.save(entity);
        logger.info("Saved Admin entity: {}", savedEntity.getId());

        return AdminMapper.mapToDomain(savedEntity);
    }
}
